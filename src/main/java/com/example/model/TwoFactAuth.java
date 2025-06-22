package com.example.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Embeddable
public class TwoFactAuth {

    private boolean isEnabled = false;

    private VerificationType sendTo;

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public VerificationType getSendTo() {
		return sendTo;
	}

	public void setSendTo(VerificationType sendTo) {
		this.sendTo = sendTo;
	}
    
    
}
