package com.guestlogix.traveleruikit.forms.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import com.guestlogix.traveleruikit.forms.Form;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public abstract class SectionedFormAdapter<S, F> extends Form.FormAdapter {
    private final static String TAG = "SectionedFormAdapter";
    private final List<S> sections;
    private Map<Either<S, F>, Integer> modelToPos;
    private Map<Integer, Either<S, F>> posToModel;
    private Map<Integer, Integer> posToType;
    private int count;
    private final boolean useCaches;

    public SectionedFormAdapter(List<S> sections) {
        Log.d(TAG, "Initializing sectioned form adapter with use caches set to true");
        this.sections = sections;
        useCaches = true;
        buildSectionMappings();
    }

    public SectionedFormAdapter(List<S> sections, boolean useCaches) {
        Log.d(TAG, String.format("Initializing sectioned form adapter with use caches set to %b", useCaches));
        this.sections = sections;
        this.useCaches = useCaches;

        if (this.useCaches) {
            buildSectionMappings();
        }
    }

    @Override
    public int getItemCount() {
        if (useCaches) {
            return count;
        } else {
            return calculateSize();
        }
    }

    @Override
    public int getViewType(int position) {
        if (useCaches) {
            int type = 0;
            if (posToType.containsKey(position)) {
                Integer cachedType = posToType.get(position);

                if (cachedType == null) {
                    Log.e(TAG, String.format("Cached type for position %s is null. Please make sure to give correct keys!", position));
                } else {
                    type = cachedType;
                }
            } else {
                type = getUnknownViewType(position);
            }
            return type;
        } else {
            return getType(position);
        }
    }

    /**
     * Notifies the adapter that the data has been changed.
     */
    public void notifyDataChanged() {
        Log.i(TAG, "Rebuilding the mappings for the form");
        buildSectionMappings();
    }

    public abstract List<F> getChildren(S section);

    public abstract int getSectionType(S section);

    public abstract int getFieldType(F field);

    private int getUnknownViewType(int position) {
        Log.i(TAG, "Fetching type from activity");
        int type = 0;

        if (!posToModel.containsKey(position)) {
            Log.e(TAG, String.format("Position %d does not have a model attached to it", position));
        } else {
            Either<S, F> e = posToModel.get(position);

            if (null == e) {
                Log.e(TAG, "Position %d has a null model");
            } else {
                if (e.isLeft()) {
                    type = getSectionType(e.getLeft());
                } else {
                    type = getFieldType(e.getRight());
                }

                // Save to cache so we don't have to call again.
                posToType.put(position, type);
            }
        }

        return type;
    }

    private void buildSectionMappings() {
        Log.d(TAG, "Building section mappings");
        count = 0;
        modelToPos = new HashMap<>();
        posToModel = new HashMap<>();
        posToType = new HashMap<>();

        for (S s : sections) {
            Either<S, F> e = new Either<>();
            e.setLeft(s);
            modelToPos.put(e, count);
            posToModel.put(count, e);
            count++;
            buildFieldMappings(getChildren(s));
        }
    }

    private void buildFieldMappings(List<F> fields) {
        Log.d(TAG, "Building field mappings current index is " + count);
        for (F f : fields) {
            Either<S, F> e = new Either<>();
            e.setRight(f);
            modelToPos.put(e, count);
            posToModel.put(count, e);
            count++;
        }
    }

    private int calculateSize() {
        int size = 0;

        for(S s : sections) {
            size++;
            size += getChildren(s).size();
        }

        return size;
    }

    private int getType(int pos) {
        for (S s : sections) {
            if (pos == 0) {
                return getSectionType(s);
            }
            List<F> fields = getChildren(s);
            pos--;
            for (F f : fields) {
                if (pos == 0) {
                    return getFieldType(f);
                }
                pos--;
            }
        }
        Log.e(TAG, "Could not find section or field matching the position");
        return 0;
    }

    // Helper class to keep maps simple.
    private final class Either<L, R> {
        private final static String TAG = "Either";
        private boolean isLeft;
        private L l;
        private R r;

        Either() {
        }

        void setLeft(final L l) {
            if (null != r) {
                Log.e(TAG, "Tried setting left value when right one is not null");
            } else {
                Log.d(TAG, "locking left value");
                isLeft = true;
                this.l = l;
            }
        }

        void setRight(final R r) {
            if (null != l) {
                Log.e(TAG, "Tried setting right value when left one is not null");
            } else {
                Log.d(TAG, "locking right value");
                isLeft = false;
                this.r = r;
            }
        }

        boolean isLeft() {
            return isLeft;
        }

        L getLeft() {
            return l;
        }

        R getRight() {
            return r;
        }
    }
}
