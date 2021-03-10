package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.*;
import com.zte.msg.alarmcenter.dto.res.DeviceSlotResDTO;
import com.zte.msg.alarmcenter.dto.res.SnmpAlarmCodeResDTO;
import com.zte.msg.alarmcenter.dto.res.SnmpSlotResDTO;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.DeviceSlotMapper;
import com.zte.msg.alarmcenter.mapper.SnmpMapper;
import com.zte.msg.alarmcenter.service.SnmpService;
import com.zte.msg.alarmcenter.utils.ExcelPortUtil;
import com.zte.msg.alarmcenter.utils.FileUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class SnmpServiceImpl implements SnmpService {

    @Autowired
    private SnmpMapper mySlotMapper;

    @Autowired
    private DeviceSlotMapper myDeviceSlotMapper;

    @Override
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
                reqDTO.setSnmpSlotName(cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(CellType.STRING);
                reqDTO.setSystemId(Long.valueOf(cells.getCell(1).getStringCellValue()));
                reqDTO.setLineCode(cells.getCell(2).getStringCellValue());
                reqDTO.setSiteCode(cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(CellType.STRING);
                reqDTO.setDeviceCode(cells.getCell(4).getStringCellValue());
                reqDTO.setSlotCode(cells.getCell(5).getStringCellValue());
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
                            throw new CommonException(4000, "插入设备槽位失败！");
                        }
                    }
                }
            }
            int count = mySlotMapper.importDevice(temp, userId);
            if (count == 0) {
                throw new CommonException(4000, "批量插入snmp槽位失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportDevice(String snmpSlotName, Long systemId, Long siteId, HttpServletResponse response) {
        // 列名
        List<String> listName = Arrays.asList("SNMP槽位（必填）", "系统编号（必填）", "线路编号（必填）", "站点编号（必填）", "设备编号（必填）", "槽位编号（必填）", "槽位名称（必填）");
        List<SnmpSlotResDTO> snmpSlotResList = mySlotMapper.exportDevice(snmpSlotName, systemId, siteId, null);
        // 列名 数据
        ArrayList<Map<String, String>> list = new ArrayList<>();
        if (null != snmpSlotResList) {
            for (SnmpSlotResDTO snmpSlotResDTO : snmpSlotResList) {
                HashMap<String, String> map = new HashMap<>();
                map.put("SNMP槽位（必填）", snmpSlotResDTO.getSnmpSlotName());
                map.put("系统编号（必填）", snmpSlotResDTO.getSystemId());
                map.put("线路编号（必填）", snmpSlotResDTO.getLineCode());
                map.put("站点编号（必填）", snmpSlotResDTO.getSiteCode());
                map.put("设备编号（必填）", snmpSlotResDTO.getDeviceCode());
                map.put("槽位编号（必填）", snmpSlotResDTO.getSlotCode());
                map.put("槽位名称（必填）", snmpSlotResDTO.getSlotName());
                list.add(map);
            }
        }
        if (list.size() > 0) {
            // 将需要写入Excel的数据传入
            ExcelPortUtil.excelPort("SNMP槽位配置", listName, list, null, response);
        }
    }

    @Override
    public void addSnmpSlot(SnmpSlotModifyReqDTO slotModifyReqDTO, String userId) {
        int integer = mySlotMapper.addSnmpSlot(slotModifyReqDTO, userId);
        if (integer < 0) {
            throw new CommonException(4000,"新增失败！");
        }
    }

    @Override
    public void modifySnmpSlot(SnmpSlotModifyReqDTO slotModifyReqDTO, Long id, String userId) {
        int integer = mySlotMapper.modifySnmpSlot(slotModifyReqDTO, id,userId);
        if (integer < 0) {
            throw new CommonException(4000,"修改失败！");
        }
    }

    @Override
    public void deleteSnmpSlot(Long id) {
        int integer = mySlotMapper.deleteSnmpSlot(id);
        if (integer < 0) {
            throw new CommonException(4000,"删除失败！");
        }
    }

    @Override
    public Page<SnmpSlotResDTO> getSnmpSlot(String snmpSlotName, Long systemId, Long siteId, PageReqDTO pageReq) {
        List<SnmpSlotResDTO> snmpSlotResDTOList = null;
        int count = mySlotMapper.getSnmpSlotCount(snmpSlotName,systemId,siteId);
        Page<SnmpSlotResDTO> page = new Page<>();
        page.setCurrent(pageReq.getPage()).setPages(pageReq.getSize()).setTotal(count);
        if (count > 0) {
            pageReq.setPage((pageReq.getPage() - 1) * pageReq.getSize());
            snmpSlotResDTOList = mySlotMapper.getSnmpSlot(snmpSlotName,systemId,siteId,pageReq);
            page.setRecords(snmpSlotResDTOList);
        }
        return page;
    }

    @Override
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
                reqDTO.setCode(cells.getCell(2).getStringCellValue());
                cells.getCell(3).setCellType(CellType.STRING);
                reqDTO.setElementType(cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(CellType.STRING);
                reqDTO.setSnmpCode(cells.getCell(4).getStringCellValue());
                reqDTO.setReason(cells.getCell(5).getStringCellValue());
                temp.add(reqDTO);
            }
            fileInputStream.close();

            int count = mySlotMapper.importSnmpAlarmCode(temp, userId);
            if (count == 0) {
                throw new CommonException(4000, "批量插入snmp告警码失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportSnmpAlarmCode(String alarmCode, Long systemId, HttpServletResponse response) {
        // 列名
        List<String> listName = Arrays.asList("系统编号（必填）","线路编号（必填）","告警码（必填）","网元类型","SNMP码","SNMP告警码原因");
        List<SnmpAlarmCodeResDTO> snmpAlarmCodeList = mySlotMapper.exportSnmpAlarmCode(alarmCode,systemId);
        List<Map<String, String>> list = new ArrayList<>();
        if (null!=snmpAlarmCodeList) {
            for (SnmpAlarmCodeResDTO snmpAlarmCodeResDTO : snmpAlarmCodeList) {
                HashMap<String, String> map = new HashMap<>();
                map.put("系统编号（必填）",snmpAlarmCodeResDTO.getSystemId());
                map.put("线路编号（必填）",snmpAlarmCodeResDTO.getPositionId());
                map.put("告警码（必填）",snmpAlarmCodeResDTO.getCode());
                map.put("网元类型",snmpAlarmCodeResDTO.getElementType());
                map.put("SNMP码",snmpAlarmCodeResDTO.getSnmpCode());
                map.put("SNMP告警码原因",snmpAlarmCodeResDTO.getReason());
                list.add(map);
            }
        }
        if (list.size() > 0) {
            // 将需要写入Excel的数据传入
            ExcelPortUtil.excelPort("SNMP告警码", listName, list, null, response);
        }
    }

    @Override
    public void addSSnmpAlarmCode(SnmpAlarmCodeReqDTO snmpAlarmCode, String userId) {
        int integer = mySlotMapper.addSSnmpAlarmCode(snmpAlarmCode,userId);
        if (integer < 0) {
            throw new CommonException(4000,"新增失败！");
        }
    }

    @Override
    public void modifySnmpAlarmCode(SnmpAlarmCodeReqDTO snmpAlarmCode, Long id, String userId) {
        int integer = mySlotMapper.modifySnmpAlarmCode(snmpAlarmCode,id,userId);
        if (integer < 0) {
            throw new CommonException(4000,"编辑失败！");
        }
    }

    @Override
    public Page<SnmpAlarmCodeResDTO> getSnmpAlarmCode(String code, Long systemId, PageReqDTO pageReq) {
        List<SnmpAlarmCodeResDTO> snmpSlotResDTOList = null;
        int count = mySlotMapper.getSnmpAlarmCodeCount(code,systemId);
        Page<SnmpAlarmCodeResDTO> page = new Page<>();
        page.setCurrent(pageReq.getPage()).setPages(pageReq.getSize()).setTotal(count);
        if (count > 0) {
            pageReq.setPage((pageReq.getPage() - 1) * pageReq.getSize());
            snmpSlotResDTOList = mySlotMapper.getSnmpAlarmCode(code,systemId,pageReq);
            page.setRecords(snmpSlotResDTOList);
        }
        return page;
    }

    @Override
    public void deleteSnmpAlarmCode(Long id) {
        int integer = mySlotMapper.deleteSnmpAlarmCode(id);
        if (integer < 0) {
            throw new CommonException(4000,"删除失败！");
        }
    }
}