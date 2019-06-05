package br.imd.ngsi_ld.model;

public enum PropertyType {
	
	Property("Property"), GeoProperty("GeoProperty");

	private final String text;

    /**
     * @param text
     */
	PropertyType(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
