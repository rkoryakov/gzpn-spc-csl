package ru.gazprom_neft.example.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Location implements Serializable {

	public String country;

	public String city;

	public List<String> streets;

	public Location subLocation;

	public Location() {
	}

	public Location(String country, String city) {
		super();
		this.country = country;
		this.city = city;
	}

	public Location getSubLocation() {
		return subLocation;
	}

	public void setSubLocation(Location subLocation) {
		this.subLocation = subLocation;
	}

	public List<String> getStreets() {
		return streets;
	}

	public void setStreets(List<String> streets) {
		this.streets = streets;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		String result = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			result = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // "Location{" + "country='" + country + '\'' + ", city='" + city + '\'' + '}';

		return result;
	}
}