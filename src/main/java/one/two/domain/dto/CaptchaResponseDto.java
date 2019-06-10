package one.two.domain.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CaptchaResponseDto {
	private boolean succes;
	@JsonAlias("error-codes")
	private Set<String> errorCodes;
	public boolean isSucces() {
		return succes;
	}
	public void setSucces(boolean succes) {
		this.succes = succes;
	}
	public Set<String> getErrorCodes() {
		return errorCodes;
	}
	public void setErrorCodes(Set<String> errorCodes) {
		this.errorCodes = errorCodes;
	}
	
	
}
