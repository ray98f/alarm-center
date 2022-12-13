package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.AlarmCodeReqDTO;
import com.zte.msg.alarmcenter.dto.res.AlarmCodeResDTO;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.AlarmCodeMapper;
import com.zte.msg.alarmcenter.service.AlarmCodeService;
import com.zte.msg.alarmcenter.utils.Constants;
import com.zte.msg.alarmcenter.utils.ExcelPortUtil;
import com.zte.msg.alarmcenter.utils.FileUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class AlarmCodeServiceImpl implements AlarmCodeService {

    @Autowired
    private AlarmCodeMapper alarmCodeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importDevice(MultipartFile file, String userId) {
        try {
            FileInputStream fileInputStream = new FileInputStream(FileUtils.transferToFile(file));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            //读取第一个工作表
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            List<AlarmCodeReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                //如果当前行的行号（从0开始）未达到2（第三行）则从新循环
                if (cells.getRowNum() < 2) {
                    continue;
                }
                AlarmCodeReqDTO reqDTO = new AlarmCodeReqDTO();
                cells.getCell(0).setCellType(CellType.STRING);
                cells.getCell(1).setCellType(CellType.STRING);
                cells.getCell(2).setCellType(CellType.STRING);
                cells.getCell(3).setCellType(CellType.STRING);
                reqDTO.setPositionId(Long.valueOf(cells.getCell(0).getStringCellValue()));
                reqDTO.setSystemId(Long.valueOf(cells.getCell(1).getStringCellValue()));
                reqDTO.setAlarmCode(Long.valueOf(cells.getCell(2).getStringCellValue()));
                reqDTO.setAlarmLevelId(Long.valueOf(cells.getCell(3).getStringCellValue()));
                reqDTO.setAlarmName(cells.getCell(4).getStringCellValue());
                reqDTO.setReason(cells.getCell(5).getStringCellValue());
                reqDTO.setHandlingOpinions(cells.getCell(6).getStringCellValue());
                temp.add(reqDTO);
            }
            fileInputStream.close();
            int integer = alarmCodeMapper.importAlarmCode(temp, userId);
            if (integer == 0) {
                throw new CommonException(ErrorCode.IMPORT_DATA_EXIST);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportDevice(Long alarmCode, String alarmName, Long systemId, Long alarmLevelId, HttpServletResponse response) {
// 列名
        List<String> listName = Arrays.asList("线路编号", "系统编号", "告警码", "告警级别", "告警名称", "告警原因", "处理意见", "线路名称", "系统名称");
        List<AlarmCodeResDTO> alarmCodeResList = alarmCodeMapper.exportAlarmCode(alarmCode, alarmName, systemId, alarmLevelId, null, null);
        // 列名 数据
        List<Map<String, String>> list = new ArrayList<>();
        if (null != alarmCodeResList) {
            for (AlarmCodeResDTO alarmCodeRes : alarmCodeResList) {
                Map<String, String> map = new HashMap<>();
                map.put("线路编号", alarmCodeRes.getPositionCode().toString());
                map.put("系统编号", alarmCodeRes.getSystemCode().toString());
                map.put("告警码", alarmCodeRes.getAlarmCode());
                map.put("告警级别", alarmCodeRes.getAlarmLevelName());
                map.put("告警名称", alarmCodeRes.getAlarmName());
                map.put("告警原因", alarmCodeRes.getReason());
                map.put("处理意见", alarmCodeRes.getHandlingOpinions());
                map.put("线路名称", alarmCodeRes.getPositionName());
                map.put("系统名称", alarmCodeRes.getSystemName());
                list.add(map);
            }
        }
        // 将需要写入Excel的数据传入
        ExcelPortUtil.excelPort("告警码设置", listName, list, null, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAlarmCode(AlarmCodeReqDTO alarmCodeReqDTO, String userId) {
        Long id = alarmCodeMapper.selectAlarmCodeIsExist(alarmCodeReqDTO,null);
        if (!Objects.isNull(id)) {
            throw new CommonException(ErrorCode.ALARM_CODE_EXIST);
        }
        int integer = alarmCodeMapper.addAlarmCode(alarmCodeReqDTO, userId);
        if (integer == 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyAlarmCode(AlarmCodeReqDTO alarmCodeReqDTO, Long id, String userId) {
        Long result = alarmCodeMapper.selectAlarmCodeIsExist(alarmCodeReqDTO, id);
        if (!Objects.isNull(result)) {
            throw new CommonException(ErrorCode.ALARM_CODE_EXIST);
        }
        int integer = alarmCodeMapper.modifyAlarmCode(alarmCodeReqDTO, id, userId);
        if (integer == 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public Page<AlarmCodeResDTO> getAlarmCode(Long alarmCode, String alarmName, Long systemId, Long alarmLevelId, Long page, Long size) {
        List<AlarmCodeResDTO> deviceReqDTOList = null;
        if (alarmName.contains(Constants.PERCENT_SIGN)) {
            alarmName = "尼玛死了";
        }
        int count = alarmCodeMapper.getAlarmCodeCount(alarmCode, alarmName, systemId, alarmLevelId);
        Page<AlarmCodeResDTO> pageBean = new Page<>();
        pageBean.setCurrent(page).setPages(size).setTotal(count);
        if (count > 0) {
            page = (page - 1) * size;
            deviceReqDTOList = alarmCodeMapper.exportAlarmCode(alarmCode, alarmName, systemId, alarmLevelId, page, size);
            pageBean.setRecords(deviceReqDTOList);
        }
        return pageBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAlarmCode(Long id) {
        int result = alarmCodeMapper.selectIsAlarmCodeUse(id);
        if (result != 0) {
            throw new CommonException(ErrorCode.RESOURCE_USE);
        }
        int integer = alarmCodeMapper.deleteAlarmCode(id);
        if (integer == 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }
}
