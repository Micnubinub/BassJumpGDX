package tbs.bassjump.fragments;



public class ColorFragment extends Fragment {
    private static ListView listView;
    private static Adapter adapter;

    public ColorFragment() {
    }

    public static void setListAdapter(Adapter adapter) {
        ColorFragment.adapter = adapter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        if (adapter != null)
            listView.setAdapter(adapter);
        return view;
    }


}
