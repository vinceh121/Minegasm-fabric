package com.therainbowville.minegasm.client;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.metafetish.buttplug.client.ButtplugClientDevice;
import org.metafetish.buttplug.client.ButtplugWSClient;
import org.metafetish.buttplug.core.messages.LinearCmd;
import org.metafetish.buttplug.core.messages.RotateCmd;
import org.metafetish.buttplug.core.messages.StopAllDevices;
import org.metafetish.buttplug.core.messages.VibrateCmd;

import com.therainbowville.minegasm.config.MinegasmConfig;

public class ToyController {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final ButtplugWSClient client = new ButtplugWSClient("Minegasm");
	private static ButtplugClientDevice device = null;
	private static boolean shutDownHookAdded = false;
	public static String lastErrorMessage = "";
	public static boolean isConnected = false;
	public static double currentVibrationLevel = 0;
	private static Class<?> CMD;

	public static boolean connectDevice() {
		try {
			device = null;
			client.Disconnect();
			LOGGER.info("URL: " + MinegasmConfig.INSTANCE.serverUrl);

			client.Connect(new URI(MinegasmConfig.INSTANCE.serverUrl), true);
			client.startScanning();

			Thread.sleep(5000);
			client.requestDeviceList();

			LOGGER.info("Enumerating devices...");

			List<ButtplugClientDevice> devices = client.getDevices();

			int nDevices = devices.size();
			LOGGER.info(nDevices);

			if (nDevices < 1) {
				lastErrorMessage = "No device found";
			}

			for (ButtplugClientDevice dev : devices) {
				if (dev.getAllowedMessages().keySet().contains(VibrateCmd.class.getSimpleName())
						|| dev.getAllowedMessages().keySet().contains(LinearCmd.class.getSimpleName())) {
					LOGGER.info(dev.getName());
					device = dev;

					if (dev.getAllowedMessages().keySet().contains(VibrateCmd.class.getSimpleName())) {
						CMD = VibrateCmd.class;
					} else if (dev.getAllowedMessages().keySet().contains(LinearCmd.class.getSimpleName())) {
						CMD = LinearCmd.class;
					} else if (dev.getAllowedMessages().keySet().contains(RotateCmd.class.getSimpleName())) {
						CMD = RotateCmd.class;
					}
					break;
				}
			}

			if (Objects.nonNull(device) && !shutDownHookAdded) {
				Runtime.getRuntime().addShutdownHook(new Thread(() -> {
					try {
						LOGGER.info("Disconnecting devices...");
						client.sendMessage(new StopAllDevices());
						client.Disconnect();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}));

				shutDownHookAdded = true;
			}

			isConnected = true;
		} catch (Exception e) {
			lastErrorMessage = e.getMessage();
			e.printStackTrace();
		}

		return Objects.nonNull(device);
	}

	public static void setVibrationLevel(double level) {
		if (Objects.isNull(device))
			return;

		if (MinegasmConfig.INSTANCE.vibrate) {
			if (level == 0 && (CMD == VibrateCmd.class || CMD == RotateCmd.class)) {
				try {
					client.sendMessage(new StopAllDevices());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				_setVibrationLevel(level);
			}
		} else {
			if (currentVibrationLevel > 0) {
				_setVibrationLevel(level);
			}
		}
	}

	private static void _setVibrationLevel(double level) {
		try {
			if (CMD == VibrateCmd.class) {
				VibrateCmd cmd = new VibrateCmd();
				cmd.setDeviceIndex(device.getIndex());
				cmd.setSpeeds(Collections.singletonList(new VibrateCmd.Speed(0, level)));
				client.sendDeviceMessage(device, cmd);
			} else if (CMD == LinearCmd.class) {
				LinearCmd cmd = new LinearCmd();
				cmd.setDeviceIndex(device.getIndex());
				cmd.setVectors(Collections.singletonList(new LinearCmd.Vector(0, 0, level)));
				client.sendDeviceMessage(device, cmd);
			} else if (CMD == RotateCmd.class) {
				RotateCmd cmd = new RotateCmd();
				cmd.setDeviceIndex(device.getIndex());
				RotateCmd.Rotation rot = new RotateCmd.Rotation(); // TODO fix this missing constructor in buttplug4j
				rot.setSpeed(level);
				cmd.setRotations(Collections.singletonList(rot));
				client.sendDeviceMessage(device, cmd);
			}
			currentVibrationLevel = level;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getDeviceName() {
		return (Objects.nonNull(device)) ? device.getName() : "<none>";
	}

	public static long getDeviceId() {
		return (Objects.nonNull(device)) ? device.getIndex() : -1;
	}

	public static String getLastErrorMessage() {
		return lastErrorMessage;
	}
}
