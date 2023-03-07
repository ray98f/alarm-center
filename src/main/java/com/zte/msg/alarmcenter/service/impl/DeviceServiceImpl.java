package com.zte.msg.alarmcenter.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zte.msg.alarmcenter.dto.PageReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceReqDTO;
import com.zte.msg.alarmcenter.dto.req.DeviceReqModifyDTO;
import com.zte.msg.alarmcenter.dto.res.DeviceResDTO;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.mapper.DeviceMapper;
import com.zte.msg.alarmcenter.service.DeviceService;
import com.zte.msg.alarmcenter.utils.Constants;
import com.zte.msg.alarmcenter.utils.ExcelPortUtil;
import com.zte.msg.alarmcenter.utils.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper myDeviceMapper;

    @Override
    public void exportDevice(String name, String deviceCode, Long systemId, Long positionId, HttpServletResponse response) {
        List<String> listName = Arrays.asList("线路编号", "车站编号", "系统编号", "设备编号", "设备名称", "规格型号", "设备厂商");
        List<DeviceResDTO> deviceResList = myDeviceMapper.exportDevice(name, deviceCode, systemId, positionId, null, null);
        List<Map<String, String>> list = new ArrayList<>();
        if (null != deviceResList) {
            for (DeviceResDTO deviceResDTO : deviceResList) {
                Map<String, String> map = new HashMap<>();
                map.put("线路编号", deviceResDTO.getLineCode().toString());
                map.put("车站编号", deviceResDTO.getPositionCode().toString());
                map.put("系统编号", deviceResDTO.getSystemCode().toString());
                map.put("设备编号", deviceResDTO.getDeviceCode());
                map.put("设备名称", deviceResDTO.getName());
                map.put("规格型号", deviceResDTO.getBrand());
                map.put("设备厂商", deviceResDTO.getManufacturer());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("设备信息", listName, list, null, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importDevice(MultipartFile deviceFile, String userId) {
        try {
            FileInputStream fileInputStream = new FileInputStream(FileUtils.transferToFile(deviceFile));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            List<DeviceReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 2) {
                    continue;
                }
                DeviceReqDTO reqDTO = new DeviceReqDTO();
                cells.getCell(0).setCellType(CellType.STRING);
                reqDTO.setLineCode(Integer.valueOf(cells.getCell(0).getStringCellValue()));
                cells.getCell(1).setCellType(CellType.STRING);
                reqDTO.setPositionId(Long.valueOf(cells.getCell(1).getStringCellValue()));
                cells.getCell(2).setCellType(CellType.STRING);
                reqDTO.setSystemId(Long.valueOf(cells.getCell(2).getStringCellValue()));
                cells.getCell(3).setCellType(CellType.STRING);
                reqDTO.setDeviceCode(cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(CellType.STRING);
                reqDTO.setName(cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(CellType.STRING);
                reqDTO.setBrand(cells.getCell(5).getStringCellValue());
                cells.getCell(6).setCellType(CellType.STRING);
                reqDTO.setManufacturer(cells.getCell(6).getStringCellValue());
                temp.add(reqDTO);
            }
            fileInputStream.close();
            int integer = myDeviceMapper.importDevice(temp, userId);
            if (integer == 0) {
                throw new CommonException(ErrorCode.IMPORT_DATA_EXIST);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDevice(DeviceReqDTO deviceReqDTO, String userId) {
        Long id = myDeviceMapper.selectDeviceIsExist(deviceReqDTO.getName(), deviceReqDTO.getSystemId(), deviceReqDTO.getPositionId(), deviceReqDTO.getDeviceCode(), null);
        if (!Objects.isNull(id)) {
            throw new CommonException(ErrorCode.DEVICE_EXIST);
        }
        int integer = myDeviceMapper.insertDevice(deviceReqDTO, userId);
        if (integer == 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyDevice(DeviceReqModifyDTO reqModifyDTO, Long id, String userId) {
        Long result = myDeviceMapper.selectDeviceIsExist(reqModifyDTO.getName(), reqModifyDTO.getSystemId(), reqModifyDTO.getPositionId(), reqModifyDTO.getDeviceCode(), id);
        if (!Objects.isNull(result)) {
            throw new CommonException(ErrorCode.DEVICE_EXIST);
        }
        int integer = myDeviceMapper.modifyDevice(reqModifyDTO, id, userId);
        if (integer == 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public Page<DeviceResDTO> getDevices(String name, String deviceCode, Long systemId, Long positionId, Long page, Long size) {
        if (name.contains(Constants.PERCENT_SIGN)) {
            name = "Prohibit input";
        }
        PageHelper.startPage(page.intValue(), size.intValue());
        List<DeviceResDTO> deviceReqDTOList = null;
        int count = myDeviceMapper.getDevicesCount(name, deviceCode, systemId, positionId);
        Page<DeviceResDTO> pageBean = new Page<>();
        pageBean.setCurrent(page).setPages(size).setTotal(count);
        if (count > 0) {
            page = (page - 1) * size;
            deviceReqDTOList = myDeviceMapper.exportDevice(name, deviceCode, systemId, positionId, page, size);
            pageBean.setRecords(deviceReqDTOList);
        }
        return pageBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDevice(Long id) {
        Integer result1 = myDeviceMapper.selectIsDeviceUse1(id);
        Integer result2 = myDeviceMapper.selectIsDeviceUse2(id);
        if (result1 != 0 || result2 != 0) {
            throw new CommonException(ErrorCode.RESOURCE_USE);
        }
        int integer = myDeviceMapper.deleteDevice(id);
        if (integer == 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }
}
