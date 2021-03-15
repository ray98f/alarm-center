package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zte.msg.alarmcenter.dto.req.DicReqDTO;
import com.zte.msg.alarmcenter.dto.req.DicUpdateReqDTO;
import com.zte.msg.alarmcenter.dto.res.DicResDTO;
import com.zte.msg.alarmcenter.entity.Dic;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.DicMapper;
import com.zte.msg.alarmcenter.service.DicService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/19 9:54
 */
@Service
public class DicServiceImpl extends ServiceImpl<DicMapper, Dic> implements DicService {

    @Override
    public Page<DicResDTO> getDics(Page<DicResDTO> page, String name, String type, Integer isEnable) {
        Page<DicResDTO> dicResDTOPage = getBaseMapper().selectDicByPage(page, name, type, isEnable);
        dicResDTOPage.getRecords().sort(Comparator.comparing(DicResDTO::getUpdatedAt).reversed());
        return dicResDTOPage;
    }

    @Override
    public void addDic(DicReqDTO reqDTO) {
        if (getBaseMapper().selectCount(new LambdaQueryWrapper<Dic>().eq(Dic::getType, reqDTO.getType())) > 0) {
            throw new CommonException(ErrorCode.DIC_TYPE_ALREADY_EXIST, reqDTO.getType());
        }
        Dic dic = new Dic();
        BeanUtils.copyProperties(reqDTO, dic);
        getBaseMapper().insert(dic);
    }

    @Override
    public void updateDic(DicUpdateReqDTO reqDTO) {
        Dic dic1 = getBaseMapper().selectOne(new LambdaQueryWrapper<Dic>().eq(Dic::getType, reqDTO.getType()));
        if (Objects.nonNull(dic1) && !dic1.getId().equals(reqDTO.getId())) {
            throw new CommonException(ErrorCode.DIC_TYPE_ALREADY_EXIST, reqDTO.getType());
        }
        Dic dic = new Dic();
        BeanUtils.copyProperties(reqDTO, dic);
        getBaseMapper().updateById(dic);
    }

    @Override
    public void deleteDic(Long[] ids) {
        getBaseMapper().deleteBatchIds(Arrays.asList(ids));
    }

}
