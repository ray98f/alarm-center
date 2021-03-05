package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceSlotReqDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceResDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceSlotResDTO;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.DeviceSlotMapper;
import com.zte.msg.alarmcenter.service.DeviceSlotService;
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
public class DeviceSlotServiceImpl implements DeviceSlotService {

    @Autowired
    private DeviceSlotMapper myDeviceSlotMapper;

    @Override
    public void importDevice(MultipartFile deviceFile, String userId) {
        try {
            FileInputStream fileInputStream = new FileInputStream(FileUtils.transferToFile(deviceFile));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            //读取第一个工作表
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            List<DeviceSlotReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                //如果当前行的行号（从0开始）未达到2（第三行）则从新循环
                if (cells.getRowNum() < 2) {
                    continue;
                }
                DeviceSlotReqDTO reqDTO = new DeviceSlotReqDTO();
                cells.getCell(0).setCellType(CellType.STRING);
                reqDTO.setSystemCode(cells.getCell(0).getStringCellValue());
                reqDTO.setLineCode(cells.getCell(1).getStringCellValue());
                reqDTO.setStationCode(cells.getCell(2).getStringCellValue());
                reqDTO.setDeviceCode(cells.getCell(3).getStringCellValue());
                reqDTO.setSlotCode(cells.getCell(4).getStringCellValue());
                reqDTO.setSlotName(cells.getCell(5).getStringCellValue());
                temp.add(reqDTO);
            }
            fileInputStream.close();
            int integer = myDeviceSlotMapper.importDevice(temp, userId);
            if (integer == 0) {
                throw new CommonException(4000, "批量插入失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportDevice(String slotName, String deviceName, String deviceCode, Long systemId, Long positionId, HttpServletResponse response) {
        // 列名
        List<String> listName = Arrays.asList("设备名称", "所属系统", "设备位置", "设备编号", "槽位编号", "槽位名称");
        List<DeviceSlotResDTO> deviceSlotResList = myDeviceSlotMapper.exportDevice(slotName, deviceName, deviceCode, systemId, positionId, null);
        // 列名 数据
        ArrayList<Map<String, String>> list = new ArrayList<>();
        if (null != deviceSlotResList) {
            for (DeviceSlotResDTO deviceSlotResDTO : deviceSlotResList) {
                Map<String, String> map = new HashMap<>();
                HashMap<String, String> hashMap = new HashMap<String, String>();
                map.put("设备名称（必填）", deviceSlotResDTO.getDeviceName());
                map.put("所属系统（必填）", deviceSlotResDTO.getSystemName());
                map.put("设备位置（必填）", deviceSlotResDTO.getPositionName());
                map.put("设备编号（必填）", deviceSlotResDTO.getDeviceCode());
                map.put("槽位编号（必填）", deviceSlotResDTO.getSlotCode());
                map.put("槽位名称（必填）", deviceSlotResDTO.getSlotName());
                list.add(map);
            }
        }
        if (list.size() > 0) {
            // 将需要写入Excel的数据传入
            ExcelPortUtil.excelPort("槽位信息（各子系统补充）", listName, list, null, response);
        }
    }

    @Override
    public void deleteDevice(Long id) {
        int integer = myDeviceSlotMapper.deleteDevice(id);
        if (integer == 0) {
            throw new CommonException(4000, "删除失败！");
        }
    }

    @Override
    public void addDeviceSlot(DeviceSlotReqDTO deviceSlotReqDTO, String userId) {
        int integer = myDeviceSlotMapper.addDeviceSlot(deviceSlotReqDTO, userId);
        if (integer == 0) {
            throw new CommonException(4000, "新增失败！");
        }
    }

    @Override
    public void modifyDevice(Long id, DeviceSlotReqDTO deviceSlotReqDTO, String userId) {
        int integer = myDeviceSlotMapper.modifyDevice(id, deviceSlotReqDTO, userId);
        if (integer == 0) {
            throw new CommonException(4000, "编辑失败！");
        }
    }

    @Override
    public Page<DeviceSlotResDTO> getDevicesSlot(String slotName, String deviceName, String deviceCode, Long systemId, Long positionId, PageReqDTO pageReq) {
        List<DeviceSlotResDTO> deviceReqDTOList = null;
        int count = myDeviceSlotMapper.getDevicesSlotCount(slotName, deviceCode, deviceCode, systemId, positionId, pageReq);
        Page<DeviceSlotResDTO> page = new Page<>();
        page.setCurrent(pageReq.getPage()).setPages(pageReq.getSize()).setTotal(count);
        if (count > 0) {
            pageReq.setPage((pageReq.getPage() - 1) * pageReq.getSize());
            deviceReqDTOList = myDeviceSlotMapper.exportDevice(slotName, deviceCode, deviceCode, systemId, positionId, pageReq);
            page.setRecords(deviceReqDTOList);
        }
        return page;
    }

}
