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
        // 列名
        List<String> listName = Arrays.asList("设备名称", "所属系统编号", "设备位置编号", "设备编号", "品牌型号", "设备串号", "设备描述");
        List<DeviceResDTO> deviceResList = myDeviceMapper.exportDevice(name, deviceCode, systemId, positionId, null, null);
        // 列名 数据
        List<Map<String, String>> list = new ArrayList<>();
        if (null != deviceResList) {
            for (DeviceResDTO deviceResDTO : deviceResList) {
                Map<String, String> map = new HashMap<>();
                map.put("设备名称", deviceResDTO.getName());
                map.put("所属系统编号", deviceResDTO.getSystemCode().toString());
                map.put("设备位置编号", deviceResDTO.getPositionCode().toString());
                map.put("设备编号", deviceResDTO.getDeviceCode());
                map.put("品牌型号", deviceResDTO.getBrand());
                map.put("设备串号", deviceResDTO.getSerialNum());
                map.put("设备描述", deviceResDTO.getDescription());
                list.add(map);
            }
        }
        // 将需要写入Excel的数据传入
        ExcelPortUtil.excelPort("设备信息", listName, list, null, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importDevice(MultipartFile deviceFile, String userId) {
        try {
            FileInputStream fileInputStream = new FileInputStream(FileUtils.transferToFile(deviceFile));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            //读取第一个工作表
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            List<DeviceReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                //如果当前行的行号（从0开始）未达到2（第三行）则从新循环
                if (cells.getRowNum() < 2) {
                    continue;
                }
                DeviceReqDTO reqDTO = new DeviceReqDTO();
                cells.getCell(0).setCellType(CellType.STRING);
                reqDTO.setName(cells.getCell(0).getStringCellValue());
                // 将列设置为字符串类型
                cells.getCell(1).setCellType(CellType.STRING);
                reqDTO.setSystemId(Long.valueOf(cells.getCell(1).getStringCellValue()));
                cells.getCell(2).setCellType(CellType.STRING);
                reqDTO.setPositionId(Long.valueOf(cells.getCell(2).getStringCellValue()));
                cells.getCell(3).setCellType(CellType.STRING);
                reqDTO.setDeviceCode(cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(CellType.STRING);
                reqDTO.setBrand(cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(CellType.STRING);
                reqDTO.setSerialNum(cells.getCell(5).getStringCellValue());
                cells.getCell(6).setCellType(CellType.STRING);
                reqDTO.setDescription(cells.getCell(6).getStringCellValue());
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

//        File files = FileUtils.transferToFile(deviceFile);
//        List<DeviceReqDTO> temp = new ArrayList<>();
//        Workbook wb0 = null;
//        try {
//            FileInputStream fileIn = new FileInputStream(files);
//            wb0 = new HSSFWorkbook(fileIn);
//            //获取Excel文档中的第一个表单
//            Sheet sht0 = wb0.getSheetAt(0);
//            for (Row cells : sht0) {
//                //如果当前行的行号（从0开始）未达到2（第三行）则从新循环
//                if(cells.getRowNum()<1){
//                    continue;
//                }
//                DeviceReqDTO reqDTO = new DeviceReqDTO();
//                reqDTO.setName(cells.getCell(0).getStringCellValue());
//                reqDTO.setSystemId(Long.valueOf(cells.getCell(1).getStringCellValue()));
//                reqDTO.setPositionId(Long.valueOf(cells.getCell(2).getStringCellValue()));
//                reqDTO.setDeviceCode(cells.getCell(3).getStringCellValue());
//                reqDTO.setBrand(cells.getCell(4).getStringCellValue());
//                reqDTO.setSerialNum(cells.getCell(5).getStringCellValue());
//                reqDTO.setDescription(cells.getCell(6).getStringCellValue());
//                temp.add(reqDTO);
//            }
//            fileIn.close();
//            myDeviceMapper.importDevice(temp);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDevice(DeviceReqDTO deviceReqDTO, String userId) {
        Long id = myDeviceMapper.selectDeviceIsExist(deviceReqDTO.getName(), deviceReqDTO.getSystemId(), deviceReqDTO.getPositionId(), deviceReqDTO.getDeviceCode(), null);
        if (!Objects.isNull(id)) {
            throw new CommonException(ErrorCode.DEVICE_EXIST);
        }
        List<DeviceReqDTO> temp = new ArrayList<>();
        temp.add(deviceReqDTO);
        int integer = myDeviceMapper.importDevice(temp, userId);
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
            name = "尼玛死了";
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
