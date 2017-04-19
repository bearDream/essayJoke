package AsyncTask;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import netOperate.NetOperate;

/**
 * Created by soft01 on 2017/4/19.
 */

public class NetAsyncTask extends AsyncTask<Integer,Integer,String> {

    private TextView textView;
    private ProgressBar progressBar;

    public NetAsyncTask(TextView textView, ProgressBar progressBar) {
        super();
        this.textView = textView;
        this.progressBar = progressBar;
    }

    @Override
    protected String doInBackground(Integer[] params) {
        NetOperate netOperate = new NetOperate();
        int i;
        for (i = 0; i < 10; i++) {
            netOperate.operate();
            publishProgress(i);
        }
        return i + params[0].intValue() + "";
    }

    @Override
    protected void onPostExecute(String s) {
        textView.setText("异步操作执行结束"+s);
    }

    @Override
    protected void onPreExecute() {
        textView.setText("异步操作开始");
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int value = values[0];
        progressBar.setProgress(value);
    }
}
