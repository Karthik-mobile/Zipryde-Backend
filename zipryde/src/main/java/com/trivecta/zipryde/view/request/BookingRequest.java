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
	"noOfPassengers"
})
public class BookingRequest {

	@JsonProperty("bookingId")
	private Number bookingId;
	
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
	private Number offeredPricePercentage;
		
	@JsonProperty("acceptedPrice")
	private Double acceptedPrice;
	
	@JsonProperty("bookingStatus")
	private String bookingStatus;
	
	@JsonProperty("driverStatus")
	private String driverStatus;
	
	@JsonProperty("noOfPassengers")
	private Number noOfPassengers;

	@JsonProperty("customerId")
	private Number customerId;

	@JsonProperty("cabTypeId")
	private Number cabTypeId;
	
	@JsonProperty("driverId")
	private Number driverId;
	
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

	public Number getOfferedPricePercentage() {
		return offeredPricePercentage;
	}

	public void setOfferedPricePercentage(Number offeredPricePercentage) {
		this.offeredPricePercentage = offeredPricePercentage;
	}

	public Number getDriverId() {
		return driverId;
	}

	public void setDriverId(Number driverId) {
		this.driverId = driverId;
	}

	public String getBookingDateTime() {
		return bookingDateTime;
	}

	public void setBookingDateTime(String bookingDateTime) {
		this.bookingDateTime = bookingDateTime;
	}
	
	
}
