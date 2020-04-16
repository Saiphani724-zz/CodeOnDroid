package codeondroid.codeondroid;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private final int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int mNoOfTabs) {
        super(fm);
        this.mNoOfTabs = mNoOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                AllFiles allFiles = new AllFiles();
                return allFiles;
            case 1:
                PythonFiles pythonFiles = new PythonFiles();
                return pythonFiles;
            case 2:
                CppFiles cppFiles = new CppFiles();
                return cppFiles;
            case 3:
                JavaFiles javaFiles = new JavaFiles();
                return javaFiles;
            case 4:
                CFiles cFiles = new CFiles();
                return cFiles;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
