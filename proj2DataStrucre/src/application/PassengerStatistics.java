package application;

public class PassengerStatistics {
	private final String flightNumber;
	private final int totalCanceledVIP;
	private final int totalCanceledRegular;
	private final int totalCheckedInVIP;
	private final int totalCheckedInRegular;
	private final int totalBoardedVIP;
	private final int totalBoardedRegular;

	public PassengerStatistics(String flightNumber, int totalCanceledVIP, int totalCanceledRegular,
			int totalCheckedInVIP, int totalCheckedInRegular, int totalBoardedVIP, int totalBoardedRegular) {
		this.flightNumber = flightNumber;
		this.totalCanceledVIP = totalCanceledVIP;
		this.totalCanceledRegular = totalCanceledRegular;
		this.totalCheckedInVIP = totalCheckedInVIP;
		this.totalCheckedInRegular = totalCheckedInRegular;
		this.totalBoardedVIP = totalBoardedVIP;
		this.totalBoardedRegular = totalBoardedRegular;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public int getTotalCanceledVIP() {
		return totalCanceledVIP;
	}

	public int getTotalCanceledRegular() {
		return totalCanceledRegular;
	}

	public int getTotalCheckedInVIP() {
		return totalCheckedInVIP;
	}

	public int getTotalCheckedInRegular() {
		return totalCheckedInRegular;
	}

	public int getTotalBoardedVIP() {
		return totalBoardedVIP;
	}

	public int getTotalBoardedRegular() {
		return totalBoardedRegular;
	}
}