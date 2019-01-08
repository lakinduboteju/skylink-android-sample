package sg.com.temasys.skylink.sdk.sampleapp.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import sg.com.temasys.skylink.sdk.sampleapp.R;
import sg.com.temasys.skylink.sdk.sampleapp.service.model.SkylinkPeer;
import sg.com.temasys.skylink.sdk.sampleapp.setting.Config;
import sg.com.temasys.skylink.sdk.sampleapp.utils.MultiPartyFragment;

/**
 * A simple {@link MultiPartyFragment} subclass.
 * This class is responsible for display UI and get user interaction
 */
public class ChatFragment extends MultiPartyFragment implements ChatContract.View {

    private final String TAG = ChatFragment.class.getName();

    // presenter instance to implement app logic
    private ChatContract.Presenter mPresenter;

    private Button btnSendServerMessage;
    private Button btnSendP2PMessage;
    private ListView listViewChats;
    private TextView tvRoomDetails;
    private BaseAdapter adapter;
    private EditText editChatMessage;

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    //----------------------------------------------------------------------------------------------
    // Fragment life cycle methods
    //----------------------------------------------------------------------------------------------

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("[SA][Chat][onCreate]", Config.ROOM_NAME_CHAT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "[SA][Chat][onCreateView] ");

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        // get the UI controls from layout
        getControlWidgets(rootView);

        // setup the action bar
        setActionBar();

        // init the UI controls
        initControls();

        //request an initiative connection
        requestViewLayout();

        //Defining a click event listener for the button "Send Server Message"
        btnSendServerMessage.setOnClickListener(v -> {
            processSendMessage(true);
        });

        //Defining a click event listener for the button "Send Private Message"
        btnSendP2PMessage.setOnClickListener(v -> {
            processSendMessage(false);
        });

        return rootView;
    }

    @Override
    public void onDetach() {

        super.onDetach();

        // Close the room connection when this sample app is finished, so the streams can be closed.
        // I.e. already isConnected() and not changing orientation.
        // in case of changing screen orientation, do not close the connection
        if (!((ChatActivity) context).isChangingConfigurations()) {
            // Inform the presenter to implement closing the connection
            mPresenter.onViewRequestExit();

            //clear all static variables to avoid memory leak
            peerRadioGroup = null;
            peerAll = null;
            peer1 = null;
            peer2 = null;
            peer3 = null;
            peer4 = null;

            mPeers = null;
        }
    }

    //----------------------------------------------------------------------------------------------
    // Methods called from the Presenter to update UI
    //----------------------------------------------------------------------------------------------

    /**
     * Clear the chat collection to start a new conversation
     */
    @Override
    public void onPresenterRequestRefreshChatCollection() {
        //refresh adapter and listview selection
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            if (listViewChats != null) {
                listViewChats.setSelection(adapter.getCount() - 1);
            }
        }
    }

    /**
     * Clear the input from edit text
     */
    @Override
    public void onPresenterRequestClearInput() {
        editChatMessage.setText("");
    }

    /**
     * Display information about remote peer joining the room
     *
     * @param newPeer remote peer joining the room
     */
    @Override
    public void onPresenterRequestChangeUiRemotePeerJoin(SkylinkPeer newPeer) {
        addPeerRadioBtn(newPeer);
    }

    /**
     * Display information about remote peer leaves the room
     *
     * @param remotePeerId remote peer ID leaving the room
     */
    @Override
    public void onPresenterRequestChangeUiRemotePeerLeave(String remotePeerId) {
        removePeerRadioBtn(remotePeerId);
    }

    /**
     * Display information about list of peers in room
     *
     * @param peersList
     */
    @Override
    public void onPresenterRequestFillPeers(List<SkylinkPeer> peersList) {
        fillPeerRadioBtn(peersList);
    }

    /**
     * Display information about updated room details
     *
     * @param roomDetails
     */
    @Override
    public void onPresenterRequestUpdateUi(String roomDetails) {
        tvRoomDetails.setText(roomDetails);
    }

    //----------------------------------------------------------------------------------------------
    // private methods for internal process
    //----------------------------------------------------------------------------------------------

    private void getControlWidgets(View rootView) {
        listViewChats = rootView.findViewById(R.id.lv_messages);

        // The controls from MultiPartyFragment
        peerRadioGroup = rootView.findViewById(R.id.radio_grp_peers);
        peerAll = rootView.findViewById(R.id.radio_btn_peer_all);
        peer1 = rootView.findViewById(R.id.radio_btn_peer1);
        peer2 = rootView.findViewById(R.id.radio_btn_peer2);
        peer3 = rootView.findViewById(R.id.radio_btn_peer3);
        peer4 = rootView.findViewById(R.id.radio_btn_peer4);

        // The controls from chat layout
        btnSendServerMessage = rootView.findViewById(R.id.btn_send_server_message);
        btnSendP2PMessage = rootView.findViewById(R.id.btn_send_p2p_message);
        tvRoomDetails = rootView.findViewById(R.id.tv_chat_room_details);

        editChatMessage = rootView.findViewById(R.id.chatMessage);
    }

    private void setActionBar() {
        ActionBar actionBar = ((ChatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        setHasOptionsMenu(true);
    }

    private void initControls() {

        //Defining the ArrayAdapter to set items to ListView
        adapter = new ArrayAdapter<String>(context, R.layout.list_item, mPresenter.onViewRequestGetChatCollection());

        //Setting the adapter to the ListView
        listViewChats.setAdapter(adapter);
    }

    /**
     * request info to display from presenter
     * try to connect to room if not connected
     * try to update UI if connected to room
     */
    private void requestViewLayout() {
        if (mPresenter != null) {
            mPresenter.onViewRequestConnectedLayout();
        }
    }

    /**
     * process sending message to server or directly
     */
    private void processSendMessage(boolean isSentToServer) {

        // Pass null for remotePeerId to send message to all users in the room
        String remotePeerId = getPeerIdSelectedWithWarning();
        String message = editChatMessage.getText().toString();

        // Sends message using the signalling server or P2P directly
        // Inform the presenter to implement sending message
        if (isSentToServer) {
            mPresenter.onViewRequestSendServerMessage(remotePeerId, message);
        } else {
            mPresenter.onViewRequestSendP2PMessage(remotePeerId, message);
        }
    }

}