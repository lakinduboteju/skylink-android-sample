package sg.com.temasys.skylink.sdk.rtc;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Purpose of this message processor is to handle offer/answer message types
 * <p/>
 * Created by janidu on 4/5/15.
 */
class OfferAnswerMessageProcessor implements MessageProcessor {

    private static final String TAG = OfferAnswerMessageProcessor.class.getSimpleName();

    private SkylinkConnection skylinkConnection;

    @Override
    public void process(JSONObject jsonObject) throws JSONException {
        skylinkConnection.getSkylinkPeerService().receivedOfferAnswer(jsonObject.getString("mid"),
                jsonObject.getString("sdp"), jsonObject.getString("type"));
    }

    @Override
    public void setSkylinkConnection(SkylinkConnection skylinkConnection) {
        this.skylinkConnection = skylinkConnection;
    }
}