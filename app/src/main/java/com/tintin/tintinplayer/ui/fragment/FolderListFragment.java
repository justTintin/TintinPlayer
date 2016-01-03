package com.tintin.tintinplayer.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tintin.module.stickyheadersrecyclerviewlibrary.StickyRecyclerHeadersDecoration;
import com.tintin.module.stickyheadersrecyclerviewlibrary.StickyRecyclerHeadersTouchListener;
import com.tintin.tintinplayer.R;
import com.tintin.tintinplayer.controller.FileOperateController;
import com.tintin.tintinplayer.module.VideoModule;
import com.tintin.tintinplayer.ui.Activity.VlcVideoActivity;
import com.tintin.tintinplayer.ui.fragment.dummy.DummyContent;
import com.tintin.tintinplayer.ui.view.AnimalsHeadersAdapter;
import com.tintin.tintinplayer.ui.view.DividerDecoration;
import com.tintin.tintinplayer.ui.view.RecyclerItemClickListener;

import java.util.List;

/**
 * Created by alive on 2016/1/3.
 */
public class FolderListFragment extends Fragment
{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    // TODO: Customize parameters
    private int mColumnCount = 1;

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FolderListFragment()
    {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FolderListFragment newInstance(int columnCount)
    {
        FolderListFragment fragment = new FolderListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_folder_list,
                container,
                false);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        // Set adapter populated with example dummy data
        final AnimalsHeadersAdapter adapter = new AnimalsHeadersAdapter();
        //        adapter.add("文件夹视图");
        adapter.addAll(getDummyDataSetList());
        recyclerView.setAdapter(adapter);

        // Set layout manager
        int orientation = getLayoutManagerOrientation(getResources().getConfiguration().orientation);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity(), orientation, false);
        recyclerView.setLayoutManager(layoutManager);

        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(
                adapter);
        recyclerView.addItemDecoration(headersDecor);

        // Add decoration for dividers between list items
        recyclerView.addItemDecoration(new DividerDecoration(getActivity()));

        // Add touch listeners
        StickyRecyclerHeadersTouchListener touchListener = new StickyRecyclerHeadersTouchListener(
                recyclerView, headersDecor);
        touchListener.setOnHeaderClickListener(new StickyRecyclerHeadersTouchListener.OnHeaderClickListener()
        {
            @Override
            public void onHeaderClick(View header, int position, long headerId)
            {
                Toast.makeText(getActivity(),
                        "Header position: " + position + ", id: " + headerId,
                        Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.addOnItemTouchListener(touchListener);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        Toast.makeText(getActivity(),
                                "start to play ==",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(),
                                VlcVideoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("video",
                                adapter.getItem(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        //                getActivity().startActivity(intent);
                        //                Intent intent = new Intent(Intent.ACTION_VIEW);
                        //                String type = "video/mp4";
                        //                Uri uri = Uri.parse("file:"+adapter.getItem(position).getPath());
                        //                intent.setDataAndType(uri, type);
                        //                startActivity(intent);
                    }
                }));
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver()
        {
            @Override
            public void onChanged()
            {
                headersDecor.invalidateHeaders();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener)
        {
            mListener = (OnListFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyContent.DummyItem item);
    }

    private int getLayoutManagerOrientation(int activityOrientation)
    {
        if (activityOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        {
            return LinearLayoutManager.VERTICAL;
        }
        else
        {
            return LinearLayoutManager.HORIZONTAL;
        }
    }

    private String[] getDummyDataSet()
    {
        List<VideoModule> list = FileOperateController.getVideoFile(getActivity());
        String[] strings = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
        {
            strings[i] = list.get(i).getAlbum();
        }
        return getResources().getStringArray(R.array.animals);
    }

    private List<VideoModule> getDummyDataSetList()
    {
        List<VideoModule> list = FileOperateController.getVideoFile(getActivity());

        return list;
    }

}
