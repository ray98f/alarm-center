package com.zte.msg.alarmcenter.core.pusher;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zte.msg.alarmcenter.core.pusher.base.BasePusher;
import com.zte.msg.alarmcenter.core.pusher.base.Config;
import com.zte.msg.alarmcenter.core.pusher.base.Message;
import com.zte.msg.alarmcenter.core.pusher.msg.WeChatMessage;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.enums.PushMethods;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.utils.Constants;
import com.zte.msg.pushcenter.pcscript.PcScript;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/15 9:26
 */
@Slf4j
@Component
public class WeChatPusher extends BasePusher {

    @Resource
    private WechatAccessTokenService wechatAccessTokenService;

    @Resource
    private AppService appService;

    @Resource
    private WeChatTemplateMapper weChatTemplateMapper;

    @Resource
    private RestTemplate restTemplate;

    private static final String ACCESS_TOKEN_URL =
            "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={1}&secret={2}";

    private static final String WECHAT_PUSH_URL =
            "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={1}";

    /**
     * token过期时间，腾讯为7200s，这里设置为7100s，防止因网络延迟导致访问时携带已过期token
     */
    private static final long ACCESS_TOKEN_EXPIRE = 7100000;

    /**
     * key为公众号的AppId
     */
    private final Map<String, AccessToken> accessTokens = new HashMap<>();

    @Override
    protected void init() {
        configMap.put(PushMethods.WECHAT, new HashMap<>(16));
        List<WxConfigModel> wxConfigModels = weChatTemplateMapper.selectWxConfigsForInit();
        buildAndFlush(wxConfigModels);

        List<WechatAccessToken> wechatAccessTokens = wechatAccessTokenService.getBaseMapper()
                .selectList(new QueryWrapper<>());
        accessTokens.putAll(wechatAccessTokens.stream().map(o -> {
            AccessToken accessToken = new AccessToken();
            BeanUtils.copyProperties(o, accessToken);
            return accessToken;
        }).collect(Collectors.toMap(AccessToken::getAppId, o -> o)));
    }

    @Override
    public void push(Message message) {
        WeChatMessage weChatMessage = (WeChatMessage) message;
        CompletableFuture.supplyAsync(() -> {
            WxConfig config = (WxConfig) configMap.get(PushMethods.WECHAT)
                    .get(weChatMessage.getTemplateId()).get(weChatMessage.getTemplateId().intValue());
            weChatMessage.setProviderName(config.getProviderName());
            weChatMessage.setTransmitTime(new Timestamp(System.currentTimeMillis()));
            String accessKey = getAccessKey(config);
            Req req = new Req(weChatMessage, config);
            long start = System.currentTimeMillis();
            Res body = restTemplate.exchange(WECHAT_PUSH_URL, HttpMethod.POST, new HttpEntity<>(JSONObject.toJSON(req)), Res.class, accessKey).getBody();
            int delay = (int) (System.currentTimeMillis() - start);
            weChatMessage.setDelay(delay);
            if (Objects.isNull(body)) {
                log.error("Fail to fetch the response while push wechat message, May caused by net error.");
                throw new CommonException(ErrorCode.WECHAT_PUSH_ERROR, "net work error");
            }
            return new PcScript.Res(body.getErrCode(), body.getErrMsg());
        }).exceptionally(e -> {
            log.error("Error while send a wechat message: {}", e.getMessage());
            e.printStackTrace();
            return new PcScript.Res(-1, e.getMessage());
        }).thenAcceptAsync(o -> {
            if (o.getCode() != Constants.SUCCESS && message.getIsWarn()) {
                warn();
            }
            if (message.getIsCallBack()) {
                response(message, o);
            }
            persist(weChatMessage, o);
        }, resExecutor);
    }

    private String getAccessKey(WxConfig wxConfig) {
        long now = System.currentTimeMillis();
        AccessToken accessToken = accessTokens.get(wxConfig.getAppId());
        if (Objects.isNull(accessToken) || accessToken.getExpire() < now) {
            ResponseEntity<String> forEntity = restTemplate.getForEntity(ACCESS_TOKEN_URL, String.class,
                    wxConfig.getAppId(), wxConfig.getAppSecret());
            accessToken = JSONObject.parseObject(forEntity.getBody(), AccessToken.class);
            if (Objects.nonNull(accessToken) && StringUtils.isNotBlank(accessToken.getAccessToken())) {
                accessToken.setExpire(now + ACCESS_TOKEN_EXPIRE);
                accessToken.setAppId(wxConfig.getAppId());
                accessTokens.put(wxConfig.getAppId(), accessToken);
                log.info("inset a new access token: {}", accessToken.getAccessToken());
            } else {
                log.info("AccessToken request result: {}", forEntity.toString());
                log.error("WeChart access token request fail : {}", wxConfig.getAppId());
                throw new CommonException(-1, "WeChat access token request fail");
            }
            WechatAccessToken wechatAccessToken = new WechatAccessToken();
            wechatAccessToken.setAccessToken(accessToken.getAccessToken());
            wechatAccessToken.setExpire(accessToken.getExpire());
            wechatAccessToken.setAppId(accessToken.getAppId());

            wechatAccessTokenService.saveOrUpdate(wechatAccessToken, new LambdaQueryWrapper<WechatAccessToken>()
                    .eq(WechatAccessToken::getAppId, accessToken.getAppId()));
            accessTokens.put(wxConfig.getAppId(), accessToken);
        }
        return accessToken.getAccessToken();
    }

    @Override
    protected void persist(Message message, PcScript.Res res) {
        try {
            WeChatMessage weChatMessage = (WeChatMessage) message;

            weChatMessage.setDelay(getDelay(message));

            weChatMessage.setAppName(appService.getAppName(weChatMessage.getAppId()));
            WeChatInfo weChatInfo = new WeChatInfo(weChatMessage, res);
            historyService.addHistoryWeChat(weChatInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildAndFlush(List<WxConfigModel> wxConfigModels) {
        List<WxConfig> wxConfigs = wxConfigModels.stream().filter(o ->
                Objects.nonNull(o.getConfig()) &&
                        o.getConfig().contains("appId") &&
                        o.getConfig().contains("appSecret")).map(o -> {
            WxConfig wxConfig = new WxConfig();
            BeanUtils.copyProperties(o, wxConfig);
            JSONObject config = JSONObject.parseObject(o.getConfig());
            wxConfig.setAppId(config.getString("appId"));
            wxConfig.setAppSecret(config.getString("appSecret"));
            return wxConfig;
        }).collect(Collectors.toList());
        wxConfigs.forEach(this::flushConfig);
        log.info("==========update wechat config completed, update count {} ==========", wxConfigModels.size());
    }

    private void flushConfig(WxConfig wxConfig) {
        TreeMap<Integer, Config> treeMap = configMap.get(PushMethods.WECHAT).get(wxConfig.getTemplateId());
        if (Objects.isNull(treeMap)) {
            treeMap = new TreeMap<>();
        }
        treeMap.put(wxConfig.getTemplateId().intValue(), wxConfig);
        configMap.get(PushMethods.WECHAT).put(wxConfig.getTemplateId(), treeMap);
    }

    public void flushConfig(List<Provider> providers) {
        flushConfig(providers, false);
    }

    public void flushConfig(List<Provider> providers, boolean remove) {
        List<WxConfigModel> wxConfigModels = weChatTemplateMapper.selectWxConfigForFlushByProviderIds(
                providers.stream().map(Provider::getId).collect(Collectors.toList()));
        if (!remove) {
            buildAndFlush(wxConfigModels);
        } else {
            removeConfig(wxConfigModels.stream().map(WxConfigModel::getTemplateId).toArray(Long[]::new));
            log.info("========== delete wechat config completed, delete count: {} ==========", providers.size());
        }
    }

    public void flushConfig(Provider provider) {

        flushConfig(provider, false);
    }

    public void flushConfig(Provider provider, boolean remove) {

        flushConfig(Collections.singletonList(provider), remove);
    }

    public void flushConfig(Long templateId) {
        flushConfig(templateId, false);
    }

    public void flushConfig(Long templateId, boolean remove) {
        flushConfig(new Long[]{templateId}, remove);
    }

    public void flushConfig(Long[] templateIds) {
        flushConfig(templateIds, false);
    }

    public void flushConfig(Long[] templateIds, boolean remove) {
        if (!remove) {
            List<WxConfigModel> wxConfigModels = weChatTemplateMapper.selectSmsConfigForFlushByTemplateIds(Arrays.asList(templateIds));
            buildAndFlush(wxConfigModels);
        } else {
            removeConfig(templateIds);
        }
    }

    private void removeConfig(Long[] templateIds) {
        for (Long templateId : templateIds) {
            configMap.get(PushMethods.WECHAT).remove(templateId);
        }
    }


    @Data
    protected static class WxConfig implements Config {

        private Long providerId;

        private String providerName;

        private Long templateId;

        private String wechatTemplateId;

        private String title;

        private String content;

        private String appId;

        private String appSecret;

    }

    @Data
    private static class AccessToken {

        private String appId;

        // 过期的具体时间
        @JSONField(name = "expires_in")
        private Long expire;

        @JSONField(name = "access_token")
        private String accessToken;
    }

    @Data
    private static class Req {

        public Req(WeChatMessage message, WxConfig config) {
            this.miniProgram = new MiniProgramDTO();
            this.toUser = message.getOpenId();
            this.templateId = config.getWechatTemplateId();
            this.url = message.getSkipUrl();
            this.data = message.getData();
//            this.miniProgram.setAppId(config.getAppId());
        }

        @JSONField(name = "touser")
        private String toUser;
        @JSONField(name = "template_id")
        private String templateId;
        private String url;
        @JSONField(name = "miniprogram")
        private MiniProgramDTO miniProgram;
        private String data;

        @Data
        public static class MiniProgramDTO {
            @JsonProperty(value = "appid")
            private String appId;
            @JsonProperty(value = "pagepath")
            private String pagePath;
        }
    }

    @Data
    private static class Res {
        @JsonProperty("errcode")
        private Integer errCode;
        @JsonProperty("errmsg")
        private String errMsg;
    }

}
