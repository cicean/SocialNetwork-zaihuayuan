package com.gzqining.inthegarden.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzqining.inthegarden.app.injection.Injection;


@EFragment(layout = -1, inject = false)
public class AppFragment extends Fragment {
	
	private Injection injection;
	
    private Bundle savedState;

    public AppFragment() {
        super();
        if (getArguments() == null)
            setArguments(new Bundle());
    }
    
    public Injection getInjection() {
    	return injection;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	if(getClass().isAnnotationPresent(EFragment.class)) {
    		EFragment mEFragment = getClass().getAnnotation(EFragment.class);
    		if(mEFragment.layout() != -1) {
    			View view = View.inflate(getActivity(), mEFragment.layout(), null);
    			//View view = inflater.inflate(mEFragment.layout(), container, false);
    			return view;
    		}
    	}

    	return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //use inject
        if(getClass().isAnnotationPresent(EFragment.class)) {
    		EFragment mEFragment = getClass().getAnnotation(EFragment.class);
    		if(mEFragment.inject()) {
    			injection = new Injection(this, getView());
    			injection.execute();
    		}
        }
        
        // Restore State Here
        if (!restoreStateFromArguments()) {
            // First Time, Initialize something here
            onFirstTimeLaunched();
        }
    }

    /**
     * Called when the fragment is launched for the first time.
     * In the other words, fragment is now recreated.
     */

    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save State Here
        saveStateToArguments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Save State Here
        saveStateToArguments();
    }

    ////////////////////
    // Don't Touch !!
    ////////////////////

    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            if (b != null)
                b.putBundle("internalSavedViewState8954201239547", savedState);
        }
    }

    ////////////////////
    // Don't Touch !!
    ////////////////////

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b != null) {
            savedState = b.getBundle("internalSavedViewState8954201239547");
            if (savedState != null) {
                restoreState();
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////
    // Restore Instance State Here
    /////////////////////////////////

    private void restoreState() {
        if (savedState != null) {
            // For Example
            //tv1.setText(savedState.getString("text"));
            onRestoreState(savedState);
        }
    }


    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */

    protected void onRestoreState(Bundle savedInstanceState) {
    	
    }

    //////////////////////////////
    // Save Instance State Here
    //////////////////////////////

    private Bundle saveState() {
        Bundle state = new Bundle();
        // For Example
        //state.putString("text", tv1.getText().toString());
        onSaveState(state);
        return state;
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onRestoreState(Bundle)}.
     *
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */

    protected void onSaveState(Bundle outState) {
    	
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    
    
    
    public void showDialogFragment(boolean cancelable) {
		Bundle args = new Bundle();  
		args.putBoolean(FragmentLoading.ARG_CANCELABLE, cancelable);  
		this.showDialogFragment("dialogfragment8954201239547", args);
	}
	
	private void showDialogFragment(String tag, Bundle args) {
		tag += hashCode(); 
		Fragment prev = getFragmentManager().findFragmentByTag(tag);
		if(prev != null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.remove(prev);
			ft.commitAllowingStateLoss();
		}
		
		FragmentLoading dialog = new FragmentLoading();
		dialog.setArguments(args);
		dialog.show(getFragmentManager().beginTransaction(), tag);
	}
	
	public void removeDialogFragment() {
		this.removeDialogFragment("dialogfragment8954201239547");
	}
	
	private void removeDialogFragment(String tag) {
		tag += hashCode();
		Fragment prev = getFragmentManager().findFragmentByTag(tag);
		if(prev != null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.remove(prev);
			ft.commitAllowingStateLoss();
		}
	}

}
