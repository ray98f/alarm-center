package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceSlotReqDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceResDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceSlotResDTO;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.DeviceSlotMapper;
import com.zte.msg.alarmcenter.service.DeviceSlotService;
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
public class DeviceSlotServiceImpl implements DeviceSlotService {

    @Autowired
    private DeviceSlotMapper myDeviceSlotMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
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
                reqDTO.setLineCode(cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(CellType.STRING);
                reqDTO.setStationCode(cells.getCell(1).getStringCellValue());
                cells.getCell(2).setCellType(CellType.STRING);
                reqDTO.setSystemCode(cells.getCell(2).getStringCellValue());
                cells.getCell(3).setCellType(CellType.STRING);
                reqDTO.setDeviceCode(cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(CellType.STRING);
                reqDTO.setSlotCode(cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(CellType.STRING);
                reqDTO.setSlotName(cells.getCell(5).getStringCellValue());
                temp.add(reqDTO);
            }
            fileInputStream.close();
            int integer = myDeviceSlotMapper.importDevice(temp, userId);
            if (integer == 0) {
                throw new CommonException(ErrorCode.IMPORT_DATA_EXIST);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportDevice(String slotName, String deviceName, String deviceCode, Long systemId, Long positionId, HttpServletResponse response) {
        // 列名
        List<String> listName = Arrays.asList("设备名称", "所属系统", "设备位置", "设备编号", "槽位编号", "槽位名称");
        List<DeviceSlotResDTO> deviceSlotResList = myDeviceSlotMapper.exportDevice(slotName, deviceName, deviceCode, systemId, positionId, null, null);
        // 列名 数据
        ArrayList<Map<String, String>> list = new ArrayList<>();
        if (null != deviceSlotResList) {
            for (DeviceSlotResDTO deviceSlotResDTO : deviceSlotResList) {
                Map<String, String> map = new HashMap<>();
                map.put("设备名称", deviceSlotResDTO.getDeviceName());
                map.put("所属系统", deviceSlotResDTO.getSystemName());
                map.put("设备位置", deviceSlotResDTO.getPositionName());
                map.put("设备编号", deviceSlotResDTO.getDeviceCode());
                map.put("槽位编号", deviceSlotResDTO.getSlotCode());
                map.put("槽位名称", deviceSlotResDTO.getSlotName());
                list.add(map);
            }
        }
        if (list.size() > 0) {
            // 将需要写入Excel的数据传入
            ExcelPortUtil.excelPort("槽位信息（各子系统补充）", listName, list, null, response);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDevice(Long id) {
        int result1 = myDeviceSlotMapper.selectIsDeviceSlotUse(id);
        int result2 = myDeviceSlotMapper.selectIsDeviceSlotUse2(id);
        if (result1 != 0 || result2 !=0) {
            throw new CommonException(ErrorCode.RESOURCE_USE);
        }
        int integer = myDeviceSlotMapper.deleteDevice(id);
        if (integer == 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDeviceSlot(DeviceSlotReqDTO deviceSlotReqDTO, String userId) {
        Integer result = myDeviceSlotMapper.selectIsDeviceSlotExist(deviceSlotReqDTO,null);
        if (!Objects.isNull(result)) {
            throw new CommonException(ErrorCode.DEVICE_SLOT_EXIST);
        }
        int integer = myDeviceSlotMapper.addDeviceSlot(deviceSlotReqDTO, userId);
        if (integer == 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyDevice(Long id, DeviceSlotReqDTO deviceSlotReqDTO, String userId) {
        Integer result = myDeviceSlotMapper.selectIsDeviceSlotExist(deviceSlotReqDTO,id);
        if (!Objects.isNull(result)) {
            throw new CommonException(ErrorCode.DEVICE_SLOT_EXIST);
        }
        int integer = myDeviceSlotMapper.modifyDevice(id, deviceSlotReqDTO, userId);
        if (integer == 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public Page<DeviceSlotResDTO> getDevicesSlot(String slotName, String deviceName, String deviceCode, Long systemId, Long positionId, Long page, Long size) {
        List<DeviceSlotResDTO> deviceReqDTOList = null;
        if (slotName != null && slotName.contains(Constants.PERCENT_SIGN)) {
            slotName = "尼玛死了";
        }
        if (deviceName != null && deviceName.contains(Constants.PERCENT_SIGN)) {
            deviceName = "尼玛死了";
        }
        int count = myDeviceSlotMapper.getDevicesSlotCount(slotName, deviceName, deviceCode, systemId, positionId);
        Page<DeviceSlotResDTO> pageBean = new Page<>();
        pageBean.setCurrent(page).setPages(size).setTotal(count);
        if (count > 0) {
//            pageReq.setPage((pageReq.getPage() - 1) * pageReq.getSize());
            page = (page - 1) * size;
            deviceReqDTOList = myDeviceSlotMapper.exportDevice(slotName, deviceName, deviceCode, systemId, positionId, page, size);
            pageBean.setRecords(deviceReqDTOList);
        }
        return pageBean;
    }
}
