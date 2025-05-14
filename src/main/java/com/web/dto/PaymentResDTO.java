package com.web.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResDTO implements Serializable {
		private String Status;
		private String Message;
		private String URL;
}
