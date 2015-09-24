package hihats.electricity.api;

/**
 * Created by fredrikkindstrom on 24/09/15.
 */
class ApiDataObject {

    private String resourceSpec;
    private String timestamp;
    private String value;
    private String gatewayId;

    public ApiDataObject(String resourceSpec, String timestamp, String value, String gatewayId) {
        this.resourceSpec = resourceSpec;
        this.timestamp = timestamp;
        this.value = value;
        this.gatewayId = gatewayId;
    }

    public String toString() {
        return "resourceSpec=" + resourceSpec + " timestamp=" + timestamp + " value=" + value + " gatewayId=" + gatewayId;
    }

    public String getResourceSpec() {
        return resourceSpec;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getValue() {
        return value;
    }

    public String getGatewayId() {
        return gatewayId;
    }
}
