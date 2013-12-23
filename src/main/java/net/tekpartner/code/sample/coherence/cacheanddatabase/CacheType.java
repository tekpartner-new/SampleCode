package net.tekpartner.code.sample.coherence.cacheanddatabase;

public enum CacheType {
	WRITE_THROUGH("Write-Through"), WRITE_BEHIND("Write-Behind");

	private String cacheType;

	private CacheType(String s) {
		cacheType = s;
	}

	public String getStatusCode() {
		return cacheType;
	}
}
