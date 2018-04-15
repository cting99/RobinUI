package cting.com.robineeee.support.common.mvp;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class AbsBaseFragment<P extends AbsBasePresenter<U>, U extends IBaseUi> extends Fragment{

    private static final String KEY_FRAGMENT_HIDDEN = "key_fragment_hidden";

    private P mPresenter;

    public abstract P createPresenter();

    public abstract U getUi();

    protected AbsBaseFragment() {
        mPresenter = createPresenter();
    }

    /**
     * Presenter will be available after onActivityCreated().
     *
     * @return The presenter associated with this fragment.
     */
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.onUiReady(getUi());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPresenter.onRestoreInstanceState(savedInstanceState);
            if (savedInstanceState.getBoolean(KEY_FRAGMENT_HIDDEN)) {
                getFragmentManager().beginTransaction().hide(this).commit();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onUiDestroy(getUi());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
        outState.putBoolean(KEY_FRAGMENT_HIDDEN, isHidden());
    }

}
