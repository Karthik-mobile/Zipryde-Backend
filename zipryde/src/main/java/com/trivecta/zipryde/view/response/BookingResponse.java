package com.trivecta.zipryde.view.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ 
	"bookingId",
	"cabTypeId",
	"cabType",
	"vehicleNumber",
	"customerId",
	"customerName",
	"customerMobileNumber",
	"driverId",
	"driverName",
	"driverMobileNumber",
	"driverImage",
	"crnNumber",
	"from", 
	"to", 
	"geoLocationResponse",
	"bookingDateTime",
	"acceptedDateTime",
	"startDateTime", 
	"endDateTime", 
	"suggestedPrice",
	"offeredPrice",
	"acceptedPrice", 
	"bookingStatusCode", 
	"driverStatusCode",
	"bookingStatus", 
	"driverStatus",
	"noOfPassengers",
	"pageNo",
	"tollAmount",
	"tipAmount"
})

public class BookingResponse {

	@JsonProperty("bookingId")
	private Number bookingId;
	
	@JsonProperty("from")
	private String from;
	
	@JsonProperty("to")
	private String to;
	
	@JsonProperty("geoLocationResponse")
	private GeoLocationResponse geoLocationResponse;
	
	@JsonProperty("acceptedDateTime")
	private String acceptedDateTime;
	
	@JsonProperty("bookingDateTime")
	private String bookingDateTime;
	
	@JsonProperty("startDateTime")
	private String startDateTime;
	
	@JsonProperty("endDateTime")
	private String endDateTime;
	
	@JsonProperty("suggestedPrice")
	private Double suggestedPrice;
	
	@JsonProperty("offeredPrice")
	private Double offeredPrice;	
	
	@JsonProperty("acceptedPrice")
	private Double acceptedPrice;
	
	@JsonProperty("bookingStatusCode")
	private String bookingStatusCode;
	
	@JsonProperty("driverStatusCode")
	private String driverStatusCode;
	
	@JsonProperty("bookingStatus")
	private String bookingStatus;
	
	@JsonProperty("driverStatus")
	private String driverStatus;
	
	@JsonProperty("noOfPassengers")
	private Number noOfPassengers;

	@JsonProperty("customerId")
	private Number customerId;
	
	@JsonProperty("customerName")
	private String customerName;
	
	@JsonProperty("customerMobileNumber")
	private String customerMobileNumber;
	
	@JsonProperty("driverId")
	private Number driverId;
	
	@JsonProperty("driverName")
	private String driverName;
	
	@JsonProperty("driverMobileNumber")
	private String driverMobileNumber;

	@JsonProperty("cabTypeId")
	private Number cabTypeId;
	
	@JsonProperty("cabType")
	private String cabType;
	
	@JsonProperty("vehicleNumber")
	private String vehicleNumber;
	
	@JsonProperty("crnNumber")
	private String crnNumber;
	
	@JsonProperty("driverImage")
	String driverImage;
	
	@JsonProperty("pageNo")
	private Integer pageNo;
	
	@JsonProperty("tollAmount")
	private Double tollAmount;
	
	@JsonProperty("tipAmount")
	private Double tipAmount;	
		
	public Number getBookingId() {
		return bookingId;
	}

	public void setBookingId(Number bookingId) {
		this.bookingId = bookingId;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public GeoLocationResponse getGeoLocationResponse() {
		return geoLocationResponse;
	}

	public void setGeoLocationResponse(GeoLocationResponse geoLocationResponse) {
		this.geoLocationResponse = geoLocationResponse;
	}

	public String getAcceptedDateTime() {
		return acceptedDateTime;
	}

	public void setAcceptedDateTime(String acceptedDateTime) {
		this.acceptedDateTime = acceptedDateTime;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public Double getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(Double suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public Double getAcceptedPrice() {
		return acceptedPrice;
	}

	public void setAcceptedPrice(Double acceptedPrice) {
		this.acceptedPrice = acceptedPrice;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getDriverStatus() {
		return driverStatus;
	}

	public void setDriverStatus(String driverStatus) {
		this.driverStatus = driverStatus;
	}

	public Number getNoOfPassengers() {
		return noOfPassengers;
	}

	public void setNoOfPassengers(Number noOfPassengers) {
		this.noOfPassengers = noOfPassengers;
	}

	public Number getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Number customerId) {
		this.customerId = customerId;
	}

	public Number getCabTypeId() {
		return cabTypeId;
	}

	public void setCabTypeId(Number cabTypeId) {
		this.cabTypeId = cabTypeId;
	}

	public Double getOfferedPrice() {
		return offeredPrice;
	}

	public void setOfferedPrice(Double offeredPrice) {
		this.offeredPrice = offeredPrice;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Number getDriverId() {
		return driverId;
	}

	public void setDriverId(Number driverId) {
		this.driverId = driverId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getCabType() {
		return cabType;
	}

	public void setCabType(String cabType) {
		this.cabType = cabType;
	}

	public String getCustomerMobileNumber() {
		return customerMobileNumber;
	}

	public void setCustomerMobileNumber(String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}

	public String getDriverMobileNumber() {
		return driverMobileNumber;
	}

	public void setDriverMobileNumber(String driverMobileNumber) {
		this.driverMobileNumber = driverMobileNumber;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getCrnNumber() {
		return crnNumber;
	}

	public void setCrnNumber(String crnNumber) {
		this.crnNumber = crnNumber;
	}

	public String getBookingDateTime() {
		return bookingDateTime;
	}

	public void setBookingDateTime(String bookingDateTime) {
		this.bookingDateTime = bookingDateTime;
	}

	public String getDriverImage() {
		return driverImage;
	}

	public void setDriverImage(String driverImage) {
		this.driverImage = driverImage;
	}

	public String getBookingStatusCode() {
		return bookingStatusCode;
	}

	public void setBookingStatusCode(String bookingStatusCode) {
		this.bookingStatusCode = bookingStatusCode;
	}

	public String getDriverStatusCode() {
		return driverStatusCode;
	}

	public void setDriverStatusCode(String driverStatusCode) {
		this.driverStatusCode = driverStatusCode;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Double getTollAmount() {
		return tollAmount;
	}

	public void setTollAmount(Double tollAmount) {
		this.tollAmount = tollAmount;
	}

	public Double getTipAmount() {
		return tipAmount;
	}

	public void setTipAmount(Double tipAmount) {
		this.tipAmount = tipAmount;
	}


}