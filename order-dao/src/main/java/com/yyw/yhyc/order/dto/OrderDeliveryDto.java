package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.OrderDelivery;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhou on 2016/7/31
 */
public class OrderDeliveryDto extends OrderDelivery {

    private static final long serialVersionUID = -7162110480396906836L;

    /* 收货人省名称 */
    private String receiveProvinceName;

    /* 收货人市名称 */
    private String receiveCityName;

    /* 收货人区县名称 */
    private String receiveRegionName;

    /* 用户收发货地址id */
    private int receiverAddressId;

    /* 订单类型  1：正常订单发货，2：补货订单发货*/
    private int orderType;

    //上传路径
    private String path;
    //文件名
    private String fileName;
    
    private boolean isSomeSend;//是否部分发货
    private String partComent; //部分发货确认说明
    
    private String selectPartDeliverty; //剩余商品是否补发货物 1：是,0：否
    
    //该属性是为部分发货使用的，key=商品编码,value=卖家发货的数量
    private Map<String, String> codeMap;
    
    private List<OrderPartDeliveryDto> partDeliveryDtoList; //部分发货实体
    
    private List<OrderPartDeliveryDto> sendDeliveryDtoList; //发货的实体
    
    
    
    


	public String getPartComent() {
		return partComent;
	}

	public void setPartComent(String partComent) {
		this.partComent = partComent;
	}

	public List<OrderPartDeliveryDto> getSendDeliveryDtoList() {
		return sendDeliveryDtoList;
	}

	public void setSendDeliveryDtoList(
			List<OrderPartDeliveryDto> sendDeliveryDtoList) {
		this.sendDeliveryDtoList = sendDeliveryDtoList;
	}

	public String getSelectPartDeliverty() {
		return selectPartDeliverty;
	}

	public void setSelectPartDeliverty(String selectPartDeliverty) {
		this.selectPartDeliverty = selectPartDeliverty;
	}

	public List<OrderPartDeliveryDto> getPartDeliveryDtoList() {
		return partDeliveryDtoList;
	}

	public void setPartDeliveryDtoList(
			List<OrderPartDeliveryDto> partDeliveryDtoList) {
		this.partDeliveryDtoList = partDeliveryDtoList;
	}

	public Map<String, String> getCodeMap() {
		return codeMap;
	}

	public void setCodeMap(Map<String, String> codeMap) {
		this.codeMap = codeMap;
	}

	public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    UserDto userDto;

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public int getReceiverAddressId() {
        return receiverAddressId;
    }

    public void setReceiverAddressId(int receiverAddressId) {
        this.receiverAddressId = receiverAddressId;
    }

    public String getReceiveProvinceName() {
        return receiveProvinceName;
    }

    public void setReceiveProvinceName(String receiveProvinceName) {
        this.receiveProvinceName = receiveProvinceName;
    }

    public String getReceiveCityName() {
        return receiveCityName;
    }

    public void setReceiveCityName(String receiveCityName) {
        this.receiveCityName = receiveCityName;
    }

    public String getReceiveRegionName() {
        return receiveRegionName;
    }

    public void setReceiveRegionName(String receiveRegionName) {
        this.receiveRegionName = receiveRegionName;
    }
    
    

    public boolean isSomeSend() {
		return isSomeSend;
	}

	public void setSomeSend(boolean isSomeSend) {
		this.isSomeSend = isSomeSend;
	}

	@Override
	public String toString() {
		return "OrderDeliveryDto [receiveProvinceName=" + receiveProvinceName
				+ ", receiveCityName=" + receiveCityName
				+ ", receiveRegionName=" + receiveRegionName
				+ ", receiverAddressId=" + receiverAddressId + ", orderType="
				+ orderType + ", path=" + path + ", fileName=" + fileName
				+ ", isSomeSend=" + isSomeSend + ", selectPartDeliverty="
				+ selectPartDeliverty + ", codeMap=" + codeMap
				+ ", partDeliveryDtoList=" + partDeliveryDtoList + ", userDto="
				+ userDto + "]";
	}
}
