class MyBaseAdapter extends BaseAdapter{
        //  得到item的总数
        @Override
        public int getCount() {
            return noteList.size();   //返回ListItem条目的总数
        }
        //得到item代表的对象
        @Override
        public Object getItem(int position) {
            return null;
        }
        //得到Item的id
        @Override
        public long getItemId(int position) {
            return 0;   //返回ListView Item的id
        }
        //得到Item的View视图
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Note note = noteList.get(position);
            View view = null;
            if(convertView == null){
                view = View.inflate(MainActivity.this,R.layout.note_list,null);
            }else {
                view = convertView;
            }

            TextView noteTitle = view.findViewById(R.id.note_title);
            noteTitle.setText(note.getTitle());
            TextView noteTime = view.findViewById(R.id.note_time);
            noteTime.setText(note.getModifiedDate());
            TextView noteContent = view.findViewById(R.id.note_content);
            noteContent.setText(note.getContent());

            return view;

        }
    }
