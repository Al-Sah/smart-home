package org.smarthome.sdk.models;

/**
 * Model {@code HubMessage} is used to describe messages hub can produce according to the {@code MessageAction}.
 *
 * @see DeviceData
 * @see MessageAction
 * @author  Al-Sah
 */
public class HubMessage<Data> {

    /**
     * action = Action.name
     */
    private MessageAction action;

    private Data data;


    /**
     * Creates a message to be sent
     *
     * @param action describes purpose of sending message
     * @param data can be presented in 2 ways:
     * <ul>
     *      <li>String - hub information</li>
     *      <li>{@code List<DeviceData>} - devices messages </li>
     * </ul>
     *
     * @throws IllegalArgumentException if action was not set
     */
    public HubMessage(MessageAction action, Data data) throws IllegalArgumentException {

        if(action == null){
            throw new IllegalArgumentException("filed 'action' is not set");
        }
        if(action != MessageAction.HEART_BEAT && data == null){
            throw new IllegalArgumentException("filed 'data' is empty or not set");
        }

        this.action = action;
        this.data = data;
    }


    public MessageAction getAction() {
        return action;
    }

    public void setAction(MessageAction action) {
        if(action == null){
            throw new IllegalArgumentException("action cannot be null");
        }
        this.action = action;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("HubMessage{action=='%s'",getAction()));
        if(data != null){
            sb.append(String.format(", data='%s'", data));
        }
        sb.append('}');
        return sb.toString();
    }
}