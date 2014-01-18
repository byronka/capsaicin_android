package com.renomad.capsaicin;

	/**
	  * Provides constants for control messages
	  * @author Byron Katz
	  */
	public enum CtlAct {
		CLIENT_WANTS_VID((byte)1),
		CLIENT_SENDING_SVR_VID((byte)2),
		OK_MSG((byte)0);

		private final byte value;

		private CtlAct(final byte newValue) {
			value = newValue;
		}

		public byte getValue() {return value;}
	}
