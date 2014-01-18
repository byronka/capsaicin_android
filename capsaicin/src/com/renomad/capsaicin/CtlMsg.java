package com.renomad.capsaicin;

	/** 
	  *  Control message for data flow to server. Needs to
	  * be encrypted and secure.  We want our communication
	  * to be opaque, but fast.
	  *
	  */
	public class CtlMsg {

		private final byte action;
		private final byte id;

		//TODO - BK - make sure that the following adheres to best
		//practices for thread safety
		//TODO - BK - set up a way to send encoded messages back and forth.
		//openssl?
		/**
		  * Creates a thread-safe object for control messages to server.
		  * actions are set by the CtlAct static object.
		  * @param action CtlAct.send or CtlAct.receive
		  * @param id the video we want
		  */
		public CtlMsg(byte action, byte id) {
			this.action = action;
			this.id = id;
		}

		/**
		  * Thread safe message object
		  * @return a throw-away byte array
		  */
		public byte[] getMessage() {
			byte[] msg = {action, id};
			return msg;
		}
	}
