package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zte.msg.alarmcenter.dto.req.DicDataReqDTO;
import com.zte.msg.alarmcenter.dto.req.DicDataUpdateReqDTO;
import com.zte.msg.alarmcenter.dto.res.DicDataResDTO;
import com.zte.msg.alarmcenter.entity.Dic;
import com.zte.msg.alarmcenter.entity.DicData;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.DicDataMapper;
import com.zte.msg.alarmcenter.mapper.DicMapper;
import com.zte.msg.alarmcenter.service.DicDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Comparator;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/19 15:04
 */
@Service
public class DicDataServiceImpl extends ServiceImpl<DicDataMapper, DicData> implements DicDataService {

    @Resource
    private DicMapper dicMapper;

    @Override
    public Page<DicDataResDTO> getDicDataByPage(Page<DicDataResDTO> page, String type, String value, Integer isEnable) {
        Page<DicDataResDTO> dicDataResDTOPage = getBaseMapper().selectDicDataByPage(page, type, value, isEnable);
        dicDataResDTOPage.getRecords().sort(Comparator.comparing(DicDataResDTO::getUpdatedAt).reversed());
        return dicDataResDTOPage;
    }

    @Override
    public void addDicData(DicDataReqDTO reqDTO) {

        if (dicMapper.selectCount(new QueryWrapper<Dic>().lambda().eq(Dic::getType, reqDTO.getType())) <= 0) {
            throw new CommonException(ErrorCode.DIC_TYPE_NOT_EXIST);
        }
        DicData dicData = new DicData();
        BeanUtils.copyProperties(reqDTO, dicData);
        getBaseMapper().insert(dicData);

    }

    @Override
    public void updateDicData(DicDataUpdateReqDTO reqDTO) {

        if (dicMapper.selectCount(new LambdaQueryWrapper<Dic>().eq(Dic::getType, reqDTO.getType())) <= 0) {
            throw new CommonException(ErrorCode.DIC_TYPE_NOT_EXIST);
        }
        DicData dicData = new DicData();
        BeanUtils.copyProperties(reqDTO, dicData);
        getBaseMapper().updateById(dicData);
    }

    @Override
    public void deleteDicData(Long[] ids) {
        getBaseMapper().deleteBatchIds(Arrays.asList(ids));
    }
}
