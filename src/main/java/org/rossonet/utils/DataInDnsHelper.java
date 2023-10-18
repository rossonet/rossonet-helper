package org.rossonet.utils;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import org.xbill.DNS.DClass;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import com.google.common.base.Splitter;

public class DataInDnsHelper {

	public static String getStringFromDns(final String hostNamePrefix, final String domain, final int retry)
			throws TextParseException, UnknownHostException {
		final StringBuilder resultString = new StringBuilder();
		final Set<String> errors = new HashSet<>();
		final Lookup l = new Lookup(hostNamePrefix + "-max" + "." + domain, Type.TXT, DClass.IN);
		l.setResolver(new SimpleResolver());
		l.run();
		if (l.getResult() == Lookup.SUCCESSFUL) {
			final int chunkSize = Integer
					.parseInt(l.getAnswers()[0].rdataToString().replaceAll("^\"", "").replaceAll("\"$", ""));
			if (chunkSize > 0) {
				for (int c = 0; c < chunkSize; c++) {
					final Lookup cl = new Lookup(hostNamePrefix + "-" + String.valueOf(c) + "." + domain, Type.TXT,
							DClass.IN);
					cl.setResolver(new SimpleResolver());
					cl.run();
					if (cl.getResult() == Lookup.SUCCESSFUL) {
						resultString
								.append(cl.getAnswers()[0].rdataToString().replaceAll("^\"", "").replaceAll("\"$", ""));
					} else {
						errors.add("error in chunk " + hostNamePrefix + "-" + String.valueOf(c) + "." + domain + " -> "
								+ cl.getErrorString());
					}
				}
			} else {
				errors.add("error, size of data is " + l.getAnswers()[0].rdataToString());
			}
		} else {
			errors.add("no " + hostNamePrefix + "-max" + domain + " record found -> " + l.getErrorString());
			if (retry > 0) {
				return getStringFromDns(hostNamePrefix, domain, retry - 1);
			} else {
				return null;
			}
		}
		if (!errors.isEmpty()) {
			throw new RuntimeException(TextHelper.joinCollection(errors, "\n"));
		} else {
			return resultString.toString();
		}
	}

	public static String toBase64ForDns(final String hostNamePrefix, String data) throws IOException {
		final Iterable<String> chunks = Splitter.fixedLength(254).split(data);
		final StringBuilder result = new StringBuilder();
		int counter = 0;
		for (final String s : chunks) {
			result.append(hostNamePrefix + "-" + String.valueOf(counter) + "\tIN\tTXT\t" + '"' + s + '"' + "\n");
			counter++;
		}
		result.append(hostNamePrefix + "-max" + "\tIN\tTXT\t" + '"' + String.valueOf(counter) + '"' + "\n");
		return result.toString();
	}

	private DataInDnsHelper() {
		throw new UnsupportedOperationException("Just for static usage");

	}

}
