package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.req.*;
import com.zte.msg.alarmcenter.dto.res.SnmpAlarmCodeResDTO;
import com.zte.msg.alarmcenter.dto.res.SnmpSlotResDTO;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.DeviceSlotMapper;
import com.zte.msg.alarmcenter.mapper.SnmpMapper;
import com.zte.msg.alarmcenter.service.SnmpService;
import com.zte.msg.alarmcenter.utils.Constants;
import com.zte.msg.alarmcenter.utils.ExcelPortUtil;
import com.zte.msg.alarmcenter.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SnmpServiceImpl implements SnmpService {

    @Autowired
    private SnmpMapper mySlotMapper;

    @Autowired
    private DeviceSlotMapper myDeviceSlotMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importDevice(MultipartFile file, String userId) {
        try {
            FileInputStream fileInputStream = new FileInputStream(FileUtils.transferToFile(file));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            //读取第一个工作表
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            List<SnmpSlotReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                //如果当前行的行号（从0开始）未达到2（第三行）则从新循环
                if (cells.getRowNum() < 2) {
                    continue;
                }
                SnmpSlotReqDTO reqDTO = new SnmpSlotReqDTO();
                cells.getCell(0).setCellType(CellType.STRING);
                reqDTO.setSnmpSlotName(cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(CellType.STRING);
                reqDTO.setSystemId(Long.valueOf(cells.getCell(1).getStringCellValue()));
                cells.getCell(2).setCellType(CellType.STRING);
                reqDTO.setLineCode(cells.getCell(2).getStringCellValue());
                cells.getCell(3).setCellType(CellType.STRING);
                reqDTO.setSiteCode(cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(CellType.STRING);
                reqDTO.setDeviceCode(cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(CellType.STRING);
                reqDTO.setSlotCode(cells.getCell(5).getStringCellValue());
                cells.getCell(6).setCellType(CellType.STRING);
                reqDTO.setSlotName(cells.getCell(6).getStringCellValue());
                temp.add(reqDTO);
            }
            fileInputStream.close();
            String str = "";
            if (temp.size() > 0) {
                for (SnmpSlotReqDTO snmpSlotReqDTO : temp) {
                    if (str.equals(snmpSlotReqDTO.getSlotCode()) || null == snmpSlotReqDTO.getSlotCode()) {
                        DeviceSlotReqDTO deviceSlotReqDTO = new DeviceSlotReqDTO();
                        deviceSlotReqDTO.setSystemCode(snmpSlotReqDTO.getSystemId().toString());
                        deviceSlotReqDTO.setLineCode(snmpSlotReqDTO.getLineCode());
                        deviceSlotReqDTO.setStationCode(snmpSlotReqDTO.getSiteCode());
                        deviceSlotReqDTO.setDeviceCode(snmpSlotReqDTO.getDeviceCode());
                        deviceSlotReqDTO.setSlotCode(null);
                        deviceSlotReqDTO.setSlotName(str.equals(snmpSlotReqDTO.getSlotName()) ? snmpSlotReqDTO.getSnmpSlotName() : snmpSlotReqDTO.getSlotName());
                        int integer = myDeviceSlotMapper.importOneDevice(deviceSlotReqDTO, userId);
                        if (integer > 0) {
                            snmpSlotReqDTO.setSlotCode(deviceSlotReqDTO.getId());
                        } else {
                            throw new CommonException(ErrorCode.IMPORT_DATA_EXIST);
                        }
                    }
                }
            }
            int count = mySlotMapper.importDevice(temp, userId);
            if (count == 0) {
                throw new CommonException(ErrorCode.IMPORT_DATA_EXIST);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportDevice(String snmpSlotName, Long systemId, Long siteId, HttpServletResponse response) {
        // 列名
        List<String> listName = Arrays.asList("SNMP槽位", "系统编号", "线路编号", "站点编号", "设备编号", "槽位编号", "槽位名称", "系统名称", "线路名称", "站点名称");
        List<SnmpSlotResDTO> snmpSlotResList = mySlotMapper.exportDevice(snmpSlotName, systemId, siteId, null);
        // 列名 数据
        ArrayList<Map<String, String>> list = new ArrayList<>();
        if (null != snmpSlotResList) {
            for (SnmpSlotResDTO snmpSlotResDTO : snmpSlotResList) {
                HashMap<String, String> map = new HashMap<>();
                map.put("SNMP槽位", snmpSlotResDTO.getSnmpSlotName());
                map.put("系统编号", snmpSlotResDTO.getSystemId());
                map.put("线路编号", snmpSlotResDTO.getPositionId());
                map.put("站点编号", snmpSlotResDTO.getSiteCode());
                map.put("设备编号", snmpSlotResDTO.getDeviceCode());
                map.put("槽位编号", snmpSlotResDTO.getSlotCode());
                map.put("槽位名称", snmpSlotResDTO.getSlotName());
                map.put("系统名称", snmpSlotResDTO.getSystemName());
                map.put("线路名称", snmpSlotResDTO.getPositionName());
                map.put("站点名称", snmpSlotResDTO.getSiteName());
                list.add(map);
            }
        }
        if (list.size() > 0) {
            // 将需要写入Excel的数据传入
            ExcelPortUtil.excelPort("SNMP槽位配置", listName, list, null, response);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSnmpSlot(SnmpSlotModifyReqDTO slotModifyReqDTO, String userId) {
        Long id = mySlotMapper.selectSnmpSlotIsExist(slotModifyReqDTO, null);
        if (!Objects.isNull(id)) {
            throw new CommonException(ErrorCode.SNMP_SLOT_EXIST);
        }
        int integer = mySlotMapper.addSnmpSlot(slotModifyReqDTO, userId);
        if (integer < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifySnmpSlot(SnmpSlotModifyReqDTO slotModifyReqDTO, Long id, String userId) {
        Long result = mySlotMapper.selectSnmpSlotIsExist(slotModifyReqDTO, id);
        if (!Objects.isNull(result)) {
            throw new CommonException(ErrorCode.SNMP_SLOT_EXIST);
        }
        int integer = mySlotMapper.modifySnmpSlot(slotModifyReqDTO, id, userId);
        if (integer < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSnmpSlot(Long id) {
        int integer = mySlotMapper.deleteSnmpSlot(id);
        if (integer < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public Page<SnmpSlotResDTO> getSnmpSlot(String snmpSlotName, Long systemId, Long siteId, Long page, Long size) {
        List<SnmpSlotResDTO> snmpSlotResDTOList = null;
        if (snmpSlotName != null && snmpSlotName.contains(Constants.PERCENT_SIGN)) {
            snmpSlotName = "Prohibit input";
        }
        int count = mySlotMapper.getSnmpSlotCount(snmpSlotName, systemId, siteId);
        Page<SnmpSlotResDTO> pageBean = new Page<>();
        pageBean.setCurrent(page).setPages(size).setTotal(count);
        if (count > 0) {
            page = (page - 1) * size;
            snmpSlotResDTOList = mySlotMapper.getSnmpSlot(snmpSlotName, systemId, siteId, page, size);
            pageBean.setRecords(snmpSlotResDTOList);
        }
        return pageBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importSnmpAlarmCode(MultipartFile file, String userId) {
        try {
            FileInputStream fileInputStream = new FileInputStream(FileUtils.transferToFile(file));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            //读取第一个工作表
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            List<SnmpAlarmCodeReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                //如果当前行的行号（从0开始）未达到2（第三行）则从新循环
                if (cells.getRowNum() < 2) {
                    continue;
                }
                SnmpAlarmCodeReqDTO reqDTO = new SnmpAlarmCodeReqDTO();
                cells.getCell(0).setCellType(CellType.STRING);
                reqDTO.setSystemId(Long.valueOf(cells.getCell(0).getStringCellValue()));
                cells.getCell(1).setCellType(CellType.STRING);
                reqDTO.setPositionId(cells.getCell(1).getStringCellValue());
                cells.getCell(2).setCellType(CellType.STRING);
                reqDTO.setCode(cells.getCell(2).getStringCellValue());
                cells.getCell(3).setCellType(CellType.STRING);
                reqDTO.setElementType(cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(CellType.STRING);
                reqDTO.setSnmpCode(cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(CellType.STRING);
                reqDTO.setReason(cells.getCell(5).getStringCellValue());
                temp.add(reqDTO);
            }
            fileInputStream.close();
            int count = mySlotMapper.importSnmpAlarmCode(temp, userId);
            if (count == 0) {
                throw new CommonException(ErrorCode.IMPORT_DATA_EXIST);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportSnmpAlarmCode(String alarmCode, Long systemId, HttpServletResponse response) {
        // 列名
        List<String> listName = Arrays.asList("系统编号", "线路编号", "告警码", "网元类型", "SNMP码", "SNMP告警码原因", "系统名称", "线路名称");
        List<SnmpAlarmCodeResDTO> snmpAlarmCodeList = mySlotMapper.exportSnmpAlarmCode(alarmCode, systemId);
        List<Map<String, String>> list = new ArrayList<>();
        if (null != snmpAlarmCodeList) {
            for (SnmpAlarmCodeResDTO snmpAlarmCodeResDTO : snmpAlarmCodeList) {
                HashMap<String, String> map = new HashMap<>();
                map.put("系统编号", snmpAlarmCodeResDTO.getSystemCode());
                map.put("线路编号", snmpAlarmCodeResDTO.getPositionCode());
                map.put("告警码", snmpAlarmCodeResDTO.getCode());
                map.put("网元类型", snmpAlarmCodeResDTO.getElementType());
                map.put("SNMP码", snmpAlarmCodeResDTO.getSnmpCode());
                map.put("SNMP告警码原因", snmpAlarmCodeResDTO.getReason());
                map.put("系统名称", snmpAlarmCodeResDTO.getSystemName());
                map.put("线路名称", snmpAlarmCodeResDTO.getPositionName());
                list.add(map);
            }
        }
        if (list.size() > 0) {
            // 将需要写入Excel的数据传入
            ExcelPortUtil.excelPort("SNMP告警码", listName, list, null, response);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSSnmpAlarmCode(SnmpAlarmCodeReqDTO snmpAlarmCode, String userId) {
        Long id = mySlotMapper.selectSnmpAlarmCodeIsExist(snmpAlarmCode, null);
        if (!Objects.isNull(id)) {
            throw new CommonException(ErrorCode.SNMP_ALARM_CODE_EXIST);
        }
        int integer = mySlotMapper.addSSnmpAlarmCode(snmpAlarmCode, userId);
        if (integer < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifySnmpAlarmCode(SnmpAlarmCodeReqDTO snmpAlarmCode, Long id, String userId) {
        Long result = mySlotMapper.selectSnmpAlarmCodeIsExist(snmpAlarmCode, id);
        if (!Objects.isNull(result)) {
            throw new CommonException(ErrorCode.SNMP_ALARM_CODE_EXIST);
        }
        int integer = mySlotMapper.modifySnmpAlarmCode(snmpAlarmCode, id, userId);
        if (integer < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public Page<SnmpAlarmCodeResDTO> getSnmpAlarmCode(String code, Long systemId, Long page, Long size) {
        List<SnmpAlarmCodeResDTO> snmpSlotResDTOList = null;
        int count = mySlotMapper.getSnmpAlarmCodeCount(code, systemId);
        Page<SnmpAlarmCodeResDTO> pageBean = new Page<>();
        pageBean.setCurrent(page).setPages(size).setTotal(count);
        if (count > 0) {
//            pageReq.setPage((pageReq.getPage() - 1) * pageReq.getSize());
            page = (page - 1) * size;
            snmpSlotResDTOList = mySlotMapper.getSnmpAlarmCode(code, systemId, page, size);
            pageBean.setRecords(snmpSlotResDTOList);
        }
        return pageBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSnmpAlarmCode(Long id) {
        int integer = mySlotMapper.deleteSnmpAlarmCode(id);
        if (integer < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }
}
