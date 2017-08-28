package com.trivecta.zipryde.view.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ 
	"bookingId",
	"cabTypeId",
	"customerId",	
	"driverId",
	"from", 
	"to", 
	"geoLocationRequest",
	"bookingDateTime",
	"acceptedDateTime",
	"startDateTime", 
	"endDateTime", 
	"suggestedPrice",
	"offeredPrice",
	"offeredPricePercentage",
	"acceptedPrice", 
	"bookingStatus", 
	"driverStatus",
	"noOfPassengers",
	"pageNo"
})
public class BookingRequest {

	@JsonProperty("bookingId")
	private Integer bookingId;
	
	@JsonProperty("from")
	private String from;
	
	@JsonProperty("to")
	private String to;
	
	@JsonProperty("geoLocationRequest")
	private GeoLocationRequest geoLocationRequest;
	
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
	
	@JsonProperty("offeredPricePercentage")
	private Integer offeredPricePercentage;
		
	@JsonProperty("acceptedPrice")
	private Double acceptedPrice;
	
	@JsonProperty("bookingStatus")
	private String bookingStatus;
	
	@JsonProperty("driverStatus")
	private String driverStatus;
	
	@JsonProperty("noOfPassengers")
	private Integer noOfPassengers;

	@JsonProperty("customerId")
	private Integer customerId;

	@JsonProperty("cabTypeId")
	private Integer cabTypeId;
	
	@JsonProperty("driverId")
	private Integer driverId;
	
	@JsonProperty("pageNo")
	private Integer pageNo;
	
	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
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

	public GeoLocationRequest getGeoLocationRequest() {
		return geoLocationRequest;
	}

	public void setGeoLocationRequest(GeoLocationRequest geoLocationRequest) {
		this.geoLocationRequest = geoLocationRequest;
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

	public Integer getNoOfPassengers() {
		return noOfPassengers;
	}

	public void setNoOfPassengers(Integer noOfPassengers) {
		this.noOfPassengers = noOfPassengers;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getCabTypeId() {
		return cabTypeId;
	}

	public void setCabTypeId(Integer cabTypeId) {
		this.cabTypeId = cabTypeId;
	}

	public Double getOfferedPrice() {
		return offeredPrice;
	}

	public void setOfferedPrice(Double offeredPrice) {
		this.offeredPrice = offeredPrice;
	}

	public Integer getOfferedPricePercentage() {
		return offeredPricePercentage;
	}

	public void setOfferedPricePercentage(Integer offeredPricePercentage) {
		this.offeredPricePercentage = offeredPricePercentage;
	}

	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	public String getBookingDateTime() {
		return bookingDateTime;
	}

	public void setBookingDateTime(String bookingDateTime) {
		this.bookingDateTime = bookingDateTime;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}	
}