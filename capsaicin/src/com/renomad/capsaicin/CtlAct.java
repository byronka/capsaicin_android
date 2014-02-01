package com.renomad.capsaicin;

	/**
	  * Provides constants for control messages
	  * @author Byron Katz
	  */
	public enum CtlAct {
		CLIENT_WANTS_VID("get"),
		CLIENT_SENDING_SVR_VID("put"),
		OK_MSG("ok");

		private String value;

		private CtlAct(String newValue) {
			value = newValue;
		}

		public String getValue() {return value;}
	}
