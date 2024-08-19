
package org.rossonet.utils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.PhysicalProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.LogicalVolumeGroup;
import oshi.hardware.NetworkIF;
import oshi.hardware.PhysicalMemory;
import oshi.hardware.PowerSource;
import oshi.hardware.Sensors;
import oshi.hardware.SoundCard;
import oshi.hardware.UsbDevice;
import oshi.hardware.VirtualMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.InternetProtocolStats;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OSService;
import oshi.software.os.OSSession;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystem.ProcessFiltering;
import oshi.software.os.OperatingSystem.ProcessSorting;
import oshi.util.FormatUtil;
import oshi.util.Util;

/**
 * need oshi lib. implementation group: 'com.github.oshi', name: 'oshi-core',
 * version: '6.4.7'
 * 
 * @Author Andrea Ambrosini - Rossonet s.c.a.r.l.
 *
 */
public class SystemInfoHelper {

	public static String getSystemInfo() {
		final List<String> oshi = new ArrayList<>();
		final SystemInfo si = new SystemInfo();
		final HardwareAbstractionLayer hal = si.getHardware();
		final OperatingSystem os = si.getOperatingSystem();
		oshi.addAll(printOperatingSystem(os));
		oshi.addAll(printComputerSystem(hal.getComputerSystem()));
		oshi.addAll(printProcessor(hal.getProcessor()));
		oshi.addAll(printMemory(hal.getMemory()));
		oshi.addAll(printCpu(hal.getProcessor()));
		oshi.addAll(printProcesses(os, hal.getMemory()));
		oshi.addAll(printServices(os));
		oshi.addAll(printSensors(hal.getSensors()));
		oshi.addAll(printPowerSources(hal.getPowerSources()));
		oshi.addAll(printDisks(hal.getDiskStores()));
		oshi.addAll(printLVgroups(hal.getLogicalVolumeGroups()));
		oshi.addAll(printFileSystem(os.getFileSystem()));
		oshi.addAll(printNetworkInterfaces(hal.getNetworkIFs()));
		oshi.addAll(printNetworkParameters(os.getNetworkParams()));
		oshi.addAll(printInternetProtocolStats(os.getInternetProtocolStats()));
		oshi.addAll(printDisplays(hal.getDisplays()));
		oshi.addAll(printUsbDevices(hal.getUsbDevices(true)));
		oshi.addAll(printSoundCards(hal.getSoundCards()));
		oshi.addAll(printGraphicsCards(hal.getGraphicsCards()));
		final StringBuilder output = new StringBuilder();
		for (int i = 0; i < oshi.size(); i++) {
			output.append(oshi.get(i));
			if (oshi.get(i) != null && !oshi.get(i).endsWith("\n")) {
				output.append('\n');
			}
		}
		return output.toString();
	}

	private static List<String> printComputerSystem(final ComputerSystem computerSystem) {
		final List<String> oshi = new ArrayList<>();
		oshi.add("System: " + computerSystem.toString());
		oshi.add(" Firmware: " + computerSystem.getFirmware().toString());
		oshi.add(" Baseboard: " + computerSystem.getBaseboard().toString());
		return oshi;
	}

	private static List<String> printCpu(final CentralProcessor processor) {
		final List<String> oshi = new ArrayList<>();
		oshi.add("Context Switches/Interrupts: " + processor.getContextSwitches() + " / " + processor.getInterrupts());

		final long[] prevTicks = processor.getSystemCpuLoadTicks();
		final long[][] prevProcTicks = processor.getProcessorCpuLoadTicks();
		oshi.add("CPU, IOWait, and IRQ ticks @ 0 sec:" + Arrays.toString(prevTicks));
		// Wait a second...
		Util.sleep(1000);
		final long[] ticks = processor.getSystemCpuLoadTicks();
		oshi.add("CPU, IOWait, and IRQ ticks @ 1 sec:" + Arrays.toString(ticks));
		final long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
		final long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
		final long sys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
		final long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
		final long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
		final long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
		final long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
		final long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
		final long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;

		oshi.add(String.format(
				"User: %.1f%% Nice: %.1f%% System: %.1f%% Idle: %.1f%% IOwait: %.1f%% IRQ: %.1f%% SoftIRQ: %.1f%% Steal: %.1f%%",
				100d * user / totalCpu, 100d * nice / totalCpu, 100d * sys / totalCpu, 100d * idle / totalCpu,
				100d * iowait / totalCpu, 100d * irq / totalCpu, 100d * softirq / totalCpu, 100d * steal / totalCpu));
		oshi.add(String.format("CPU load: %.1f%%", processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100));
		final double[] loadAverage = processor.getSystemLoadAverage(3);
		oshi.add("CPU load averages:" + (loadAverage[0] < 0 ? " N/A" : String.format(" %.2f", loadAverage[0]))
				+ (loadAverage[1] < 0 ? " N/A" : String.format(" %.2f", loadAverage[1]))
				+ (loadAverage[2] < 0 ? " N/A" : String.format(" %.2f", loadAverage[2])));
		// per core CPU
		final StringBuilder procCpu = new StringBuilder("CPU load per processor:");
		final double[] load = processor.getProcessorCpuLoadBetweenTicks(prevProcTicks);
		for (final double avg : load) {
			procCpu.append(String.format(" %.1f%%", avg * 100));
		}
		oshi.add(procCpu.toString());
		long freq = processor.getProcessorIdentifier().getVendorFreq();
		if (freq > 0) {
			oshi.add("Vendor Frequency: " + FormatUtil.formatHertz(freq));
		}
		freq = processor.getMaxFreq();
		if (freq > 0) {
			oshi.add("Max Frequency: " + FormatUtil.formatHertz(freq));
		}
		final long[] freqs = processor.getCurrentFreq();
		if (freqs[0] > 0) {
			final StringBuilder sb = new StringBuilder("Current Frequencies: ");
			for (int i = 0; i < freqs.length; i++) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(FormatUtil.formatHertz(freqs[i]));
			}
			oshi.add(sb.toString());
		}
		return oshi;
	}

	private static List<String> printDisks(final List<HWDiskStore> list) {
		final List<String> oshi = new ArrayList<>();
		oshi.add("Disks:");
		for (final HWDiskStore disk : list) {
			oshi.add(" " + disk.toString());

			final List<HWPartition> partitions = disk.getPartitions();
			for (final HWPartition part : partitions) {
				oshi.add(" |-- " + part.toString());
			}
		}
		return oshi;
	}

	private static List<String> printDisplays(final List<Display> list) {
		final List<String> oshi = new ArrayList<>();
		oshi.add("Displays:");
		int i = 0;
		for (final Display display : list) {
			oshi.add(" Display " + i + ":");
			oshi.add(String.valueOf(display));
			i++;
		}
		return oshi;
	}

	private static List<String> printFileSystem(final FileSystem fileSystem) {
		final List<String> oshi = new ArrayList<>();
		oshi.add("File System:");

		oshi.add(String.format(" File Descriptors: %d/%d", fileSystem.getOpenFileDescriptors(),
				fileSystem.getMaxFileDescriptors()));

		for (final OSFileStore fs : fileSystem.getFileStores()) {
			final long usable = fs.getUsableSpace();
			final long total = fs.getTotalSpace();
			oshi.add(String.format(
					" %s (%s) [%s] %s of %s free (%.1f%%), %s of %s files free (%.1f%%) is %s "
							+ (fs.getLogicalVolume() != null && fs.getLogicalVolume().length() > 0 ? "[%s]" : "%s")
							+ " and is mounted at %s",
					fs.getName(), fs.getDescription().isEmpty() ? "file system" : fs.getDescription(), fs.getType(),
					FormatUtil.formatBytes(usable), FormatUtil.formatBytes(fs.getTotalSpace()), 100d * usable / total,
					FormatUtil.formatValue(fs.getFreeInodes(), ""), FormatUtil.formatValue(fs.getTotalInodes(), ""),
					100d * fs.getFreeInodes() / fs.getTotalInodes(), fs.getVolume(), fs.getLogicalVolume(),
					fs.getMount()));
		}
		return oshi;
	}

	private static List<String> printGraphicsCards(final List<GraphicsCard> list) {
		final List<String> oshi = new ArrayList<>();
		oshi.add("Graphics Cards:");
		if (list.isEmpty()) {
			oshi.add(" None detected.");
		} else {
			for (final GraphicsCard card : list) {
				oshi.add(" " + String.valueOf(card));
			}
		}
		return oshi;
	}

	private static List<String> printInternetProtocolStats(final InternetProtocolStats ip) {
		final List<String> oshi = new ArrayList<>();
		oshi.add("Internet Protocol statistics:");
		oshi.add(" TCPv4: " + ip.getTCPv4Stats());
		oshi.add(" TCPv6: " + ip.getTCPv6Stats());
		oshi.add(" UDPv4: " + ip.getUDPv4Stats());
		oshi.add(" UDPv6: " + ip.getUDPv6Stats());
		return oshi;
	}

	private static List<String> printLVgroups(final List<LogicalVolumeGroup> list) {
		final List<String> oshi = new ArrayList<>();
		if (!list.isEmpty()) {
			oshi.add("Logical Volume Groups:");
			for (final LogicalVolumeGroup lvg : list) {
				oshi.add(" " + lvg.toString());
			}
		}
		return oshi;
	}

	private static List<String> printMemory(final GlobalMemory memory) {
		final List<String> oshi = new ArrayList<>();
		oshi.add("Physical Memory: \n " + memory.toString());
		final VirtualMemory vm = memory.getVirtualMemory();
		oshi.add("Virtual Memory: \n " + vm.toString());
		final List<PhysicalMemory> pmList = memory.getPhysicalMemory();
		if (!pmList.isEmpty()) {
			oshi.add("Physical Memory: ");
			for (final PhysicalMemory pm : pmList) {
				oshi.add(" " + pm.toString());
			}
		}
		return oshi;
	}

	private static List<String> printNetworkInterfaces(final List<NetworkIF> list) {
		final List<String> oshi = new ArrayList<>();
		final StringBuilder sb = new StringBuilder("Network Interfaces:");
		if (list.isEmpty()) {
			sb.append(" Unknown");
		} else {
			for (final NetworkIF net : list) {
				sb.append("\n ").append(net.toString());
			}
		}
		oshi.add(sb.toString());
		return oshi;
	}

	private static List<String> printNetworkParameters(final NetworkParams networkParams) {
		final List<String> oshi = new ArrayList<>();
		oshi.add("Network parameters:\n " + networkParams.toString());
		return oshi;
	}

	private static List<String> printOperatingSystem(final OperatingSystem os) {
		final List<String> oshi = new ArrayList<>();
		oshi.add(String.valueOf(os));
		oshi.add("Booted: " + Instant.ofEpochSecond(os.getSystemBootTime()));
		oshi.add("Uptime: " + FormatUtil.formatElapsedSecs(os.getSystemUptime()));
		oshi.add("Running with" + (os.isElevated() ? "" : "out") + " elevated permissions.");
		oshi.add("Sessions:");
		for (final OSSession s : os.getSessions()) {
			oshi.add(" " + s.toString());
		}
		return oshi;
	}

	private static List<String> printPowerSources(final List<PowerSource> list) {
		final List<String> oshi = new ArrayList<>();
		final StringBuilder sb = new StringBuilder("Power Sources: ");
		if (list.isEmpty()) {
			sb.append("Unknown");
		}
		for (final PowerSource powerSource : list) {
			sb.append("\n ").append(powerSource.toString());
		}
		oshi.add(sb.toString());
		return oshi;
	}

	private static List<String> printProcesses(final OperatingSystem os, final GlobalMemory memory) {
		final List<String> oshi = new ArrayList<>();
		final OSProcess myProc = os.getProcess(os.getProcessId());
		// current process will never be null. Other code should check for null here
		oshi.add(
				"My PID: " + myProc.getProcessID() + " with affinity " + Long.toBinaryString(myProc.getAffinityMask()));
		oshi.add("Processes: " + os.getProcessCount() + ", Threads: " + os.getThreadCount());
		// Sort by highest CPU
		final List<OSProcess> procs = os.getProcesses(ProcessFiltering.ALL_PROCESSES, ProcessSorting.CPU_DESC, 5);
		oshi.add("   PID  %CPU %MEM       VSZ       RSS Name");
		for (int i = 0; i < procs.size() && i < 5; i++) {
			final OSProcess p = procs.get(i);
			oshi.add(String.format(" %5d %5.1f %4.1f %9s %9s %s", p.getProcessID(),
					100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime(),
					100d * p.getResidentSetSize() / memory.getTotal(), FormatUtil.formatBytes(p.getVirtualSize()),
					FormatUtil.formatBytes(p.getResidentSetSize()), p.getName()));
		}
		final OSProcess p = os.getProcess(os.getProcessId());
		oshi.add("Current process arguments: ");
		for (final String s : p.getArguments()) {
			oshi.add("  " + s);
		}
		oshi.add("Current process environment: ");
		for (final Entry<String, String> e : p.getEnvironmentVariables().entrySet()) {
			oshi.add("  " + e.getKey() + "=" + e.getValue());
		}
		return oshi;
	}

	private static List<String> printProcessor(final CentralProcessor processor) {
		final List<String> oshi = new ArrayList<>();
		oshi.add(processor.toString());
		oshi.add(" Cores:");
		for (final PhysicalProcessor p : processor.getPhysicalProcessors()) {
			oshi.add("  " + (processor.getPhysicalPackageCount() > 1 ? p.getPhysicalPackageNumber() + "," : "")
					+ p.getPhysicalProcessorNumber() + ": efficiency=" + p.getEfficiency() + ", id=" + p.getIdString());
		}
		return oshi;
	}

	private static List<String> printSensors(final Sensors sensors) {
		final List<String> oshi = new ArrayList<>();
		oshi.add("Sensors: " + sensors.toString());
		return oshi;
	}

	private static List<String> printServices(final OperatingSystem os) {
		final List<String> oshi = new ArrayList<>();
		oshi.add("Services: ");
		oshi.add("   PID   State   Name");
		int i = 0;
		for (final OSService s : os.getServices()) {
			if (s.getState().equals(OSService.State.RUNNING) && i++ < 5) {
				oshi.add(String.format(" %5d  %7s  %s", s.getProcessID(), s.getState(), s.getName()));
			}
		}
		i = 0;
		for (final OSService s : os.getServices()) {
			if (s.getState().equals(OSService.State.STOPPED) && i++ < 5) {
				oshi.add(String.format(" %5d  %7s  %s", s.getProcessID(), s.getState(), s.getName()));
			}
		}
		return oshi;
	}

	private static List<String> printSoundCards(final List<SoundCard> list) {
		final List<String> oshi = new ArrayList<>();
		oshi.add("Sound Cards:");
		for (final SoundCard card : list) {
			oshi.add(" " + String.valueOf(card));
		}
		return oshi;
	}

	private static List<String> printUsbDevices(final List<UsbDevice> list) {
		final List<String> oshi = new ArrayList<>();
		oshi.add("USB Devices:");
		for (final UsbDevice usbDevice : list) {
			oshi.add(String.valueOf(usbDevice));
		}
		return oshi;
	}

	private SystemInfoHelper() {
		throw new UnsupportedOperationException("Just for static usage");
	}
}
