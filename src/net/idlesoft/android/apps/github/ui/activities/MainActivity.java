/*
 * Copyright (c) 2012 Eddie Ringle
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the
 * following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following
 * disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
  * IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.idlesoft.android.apps.github.ui.activities;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.bugsense.trace.BugSenseHandler;
import net.idlesoft.android.apps.github.R;
import net.idlesoft.android.apps.github.authenticator.AccountSelect;
import net.idlesoft.android.apps.github.authenticator.AuthConstants;
import net.idlesoft.android.apps.github.ui.fragments.DashboardFragment;

public
class MainActivity extends BaseActivity
{
	private boolean mFirstRun;

	/** Called when the activity is first created. */
	@Override
	public
	void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/* BugSense setup */
		BugSenseHandler.setup(this, "40e35a51");

		mFirstRun = mPrefs.getBoolean(PREF_FIRST_RUN, true);

		if (mFirstRun) {
			mPrefsEditor.putBoolean(PREF_FIRST_RUN, false);
			mPrefsEditor.commit();

			/* Show the Account Selection screen first, then start the show */
			startActivityForResult(new Intent(this, AccountSelect.class), 0);
			finish();
		} else {
			final String current_user_login = mPrefs.getString(PREF_CURRENT_USER_LOGIN, null);
			if (current_user_login != null) {
				mCurrentUser = new Account(current_user_login, AuthConstants.GITHUB_ACCOUNT_TYPE);
			} else {
				mCurrentUser = null;
			}
		}
	}

	@Override
	protected
	void onResume()
	{
		super.onResume();

		if (!mFirstRun) {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			DashboardFragment fragment =
					(DashboardFragment) fm.findFragmentByTag(DashboardFragment.class.getName());
			if (fragment != null) {
				return;
			} else {
				fragment = new DashboardFragment();
			}

			if (isMultiPane())
				ft.replace(R.id.fragment_navigation, fragment, DashboardFragment.class.getName());
			else
				ft.replace(R.id.fragment_container, fragment, DashboardFragment.class.getName());

			ft.commit();
		}
	}

	@Override
	protected
	void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		/*
		if (requestCode == 0) {
			/* Account selection is done, restart
			startActivity(MainActivity.class);
			finish();
		}
		*/
		super.onActivityResult(requestCode, resultCode, data);
	}
}