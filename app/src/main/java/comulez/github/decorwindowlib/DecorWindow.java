package comulez.github.decorwindowlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

/**
 * Created by Ulez on 2017/8/26.
 * Email：1104128773@qq.com
 */

public class DecorWindow {
    private static final String TAG = "DecorWindow";
    private View view;
    private FrameLayout frameLayout;
    private FrameLayout.LayoutParams lp;
    private boolean added = false;
    private boolean focus;
    private int leftMargin;
    private int topMargin;
    private int rightMargin;
    private int bottomMargin;
    private Animatesss animatesss;
    private int DEFAULT_DURATION = 300;

    private boolean showAni = true;
    private boolean hideAni = true;

    private Animator hideAnimator;
    private Animator showAnimator;

    private DecorWindow(Activity mContext) {
        frameLayout = (FrameLayout) mContext.getWindow().getDecorView();
    }

    public DecorWindow showOrHide() {
        if (!added) {
            return show();
        } else {
            dismiss(null);
            return this;
        }
    }

    private boolean animateIsRunning() {
        return (showAnimator != null && showAnimator.isRunning()) || (hideAnimator != null && hideAnimator.isRunning());
    }

    public DecorWindow show() {
        if (animateIsRunning())
            return this;
        if (added) return this;
        frameLayout.addView(view, lp);
        added = true;
        if (showAni) {
            view.setVisibility(View.INVISIBLE);
            view.post(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.VISIBLE);
                    showAnimate();
                }
            });
        }
        return this;
    }

    private Animator getShowAnimator(View view, int centerX, int centerY, float startRadius, float endRadius) {
        Animator animator = null;
        if (animatesss != null) {
            animator = animatesss.getShowAnimator(view);
        }
        if (animator == null)//default unfocus show animator ;
            animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius).setDuration(DEFAULT_DURATION);
        return animator;
    }

    private Animator getShowAnimatorFocus(View realView) {
        Animator animator = null;
        if (animatesss != null) {
            animator = animatesss.getShowAnimator(realView);
        }
        if (animator == null)//default focus show animator ;
            animator = ViewAnimationUtils.createCircularReveal(view, realView.getWidth() / 2 + leftMargin, realView.getHeight() / 2 + topMargin, 0, 1.8f * getMaxRadius(view.getWidth(), view.getHeight())).setDuration(DEFAULT_DURATION);
        return animator;
    }

    public interface Animatesss {
        Animator getShowAnimator(View view);

        Animator getHideAnimator(View view);
    }

    private int getMaxRadius(int w, int h) {
        return (int) (0.5 * Math.sqrt(w * w + h * h));
    }

    public void dismiss(View v) {
        if (animateIsRunning())
            return;
        if (!added) return;
        if (hideAni) {
            int centerX, centerY, maxRaduis;
            if (focus) {
                View realView = view;
                if (v == null) {
                    centerX = realView.getWidth() / 2;
                    centerY = realView.getHeight() / 2;
                    maxRaduis = getMaxRadius(realView.getWidth(), realView.getHeight());
                } else {
                    centerX = (int) v.getX() + v.getWidth() / 2;
                    centerY = (int) v.getY() + v.getHeight() / 2;
                    maxRaduis = getMaxRadius(realView.getWidth(), realView.getHeight()) * 2;
                }
                if (realView != null && realView.getParent() != null) {
                    hideAnimate(centerX, centerY, maxRaduis);
                }
            } else {
                if (v == null) {
                    centerX = view.getWidth() / 2;
                    centerY = view.getHeight() / 2;
                    maxRaduis = getMaxRadius(view.getWidth(), view.getHeight());
                } else {
                    centerX = (int) v.getX() + v.getWidth() / 2;
                    centerY = (int) v.getY() + v.getHeight() / 2;
                    maxRaduis = getMaxRadius(view.getWidth(), view.getHeight()) * 2;
                }
                if (view != null && view.getParent() != null) {
                    hideAnimate(centerX, centerY, maxRaduis);
                }
            }
        } else {
            removeView();
        }
    }

    private void showAnimate() {
        if (showAnimator != null && showAnimator.isRunning())
            showAnimator.cancel();
        if (focus) {
            View realView = realView();
            showAnimator = getShowAnimatorFocus(realView);
            showAnimator.start();
        } else {
            showAnimator = getShowAnimator(view, view.getWidth() / 2, view.getHeight() / 2, 0, getMaxRadius(view.getWidth(), view.getHeight()));
            showAnimator.start();
        }
    }

    private void hideAnimate(int centerX, int centerY, int maxRaduis) {
        if (!added) return;
        if (hideAnimator != null && hideAnimator.isRunning())
            hideAnimator.cancel();
        if (focus) {//default focus hide animator ;
            View realView = realView();
            hideAnimator = getHideAnimatorFocus(realView, centerX, centerY, maxRaduis);
            hideAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    removeView();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    removeView();
                }
            });
            hideAnimator.start();
        } else {//default unfocus hide animator ;
            hideAnimator = getHideAnimator(view, centerX, centerY, maxRaduis);
            hideAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    removeView();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    removeView();
                }
            });
            hideAnimator.start();
        }
    }

    private Animator getHideAnimator(View view, int centerX, int centerY, int maxRaduis) {
        Animator animator = null;
        if (animatesss != null) {
            animator = animatesss.getHideAnimator(view);
        }
        if (animator == null)
            animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, maxRaduis, 0).setDuration(DEFAULT_DURATION);
        return animator;
    }

    private Animator getHideAnimatorFocus(View view, int centerX, int centerY, int maxRaduis) {
        Animator animator = null;
        if (animatesss != null) {
            animator = animatesss.getHideAnimator(view);
        }
        if (animator == null)
            animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, maxRaduis, 0).setDuration(DEFAULT_DURATION);
        return animator;
    }

    private void removeView() {
        view.setTranslationX(0);
        view.setTranslationY(0);
        frameLayout.removeView(view);
        added = false;
    }

    public static class Builder {

        private final int width;
        private int height;
        private DecorWindow decorWindow;
        private final int statusBarHeight;
        private boolean fitStatusBar = true;
        private boolean focus;
        private int leftMargin;
        private int topMargin;
        private int rightMargin;
        private int bottomMargin;
        private View view;
        private int color;
        private final Context context;
        private Animator showAnimator;
        private Animator hideAnimator;
        private boolean showAni = true;
        private boolean hideAni = true;
        private Animatesss animatesss;

        private Builder(Activity mContext) {
            context = mContext.getBaseContext();
            decorWindow = new DecorWindow(mContext);
            statusBarHeight = getStatusBarHeight(mContext);
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            width = dm.widthPixels;
            height = dm.heightPixels;
        }

        public static Builder newBuilder(Activity mContext) {
            return new Builder(mContext);
        }


        public Builder setView(View view, @ColorInt int color) {
            this.focus = true;
            this.color = color;
            this.view = view;
            return this;
        }

        public Builder setView(View view) {
            this.view = view;
            this.focus = false;
            return this;
        }

        private int dip2px(double dipValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        }


        /**
         * @param leftP   percentage of leftMargin in width;
         * @param topP    percentage of topMargin in height;
         * @param rightP  percentage of rightMargin in width;
         * @param bottomP percentage of bottomMargin in height;
         * @return
         */
        public Builder setPosPer(double leftP, double topP, double rightP, double bottomP) {
            setPosPx((int) (width * leftP), (int) (height * (topP)), (int) (width * (1 - rightP)), (int) (height * (1 - bottomP)));
            return this;
        }


        public Builder setPosDp(double leftDp, double topDp, double rightDp, double bottomDp) {
            setPosPx(dip2px(leftDp), dip2px(topDp), width - dip2px(rightDp), height - dip2px(bottomDp));
            return this;
        }

        private Builder setPosPx(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
            this.leftMargin = leftMargin;
            this.topMargin = topMargin;
            this.rightMargin = rightMargin;
            this.bottomMargin = bottomMargin;
            return this;
        }


        public DecorWindow build() {
            decorWindow.focus = focus;
            if (fitStatusBar) {
                height -= statusBarHeight;
            }
            decorWindow.leftMargin = this.leftMargin;
            decorWindow.topMargin = this.topMargin + (fitStatusBar ? statusBarHeight : 0);
            decorWindow.rightMargin = this.rightMargin;
            decorWindow.bottomMargin = this.bottomMargin;
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            decorWindow.lp = lp;
            if (focus) {//不可点击外面的,放在container中；
                LinearLayout container = new LinearLayout(view.getContext());
                container.setBackgroundColor(color);
                container.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                decorWindow.view = container;
                decorWindow.view.setPadding(decorWindow.leftMargin, decorWindow.topMargin, decorWindow.rightMargin, decorWindow.bottomMargin);
            } else {//可点击外面的
                decorWindow.view = view;
                lp.leftMargin = decorWindow.leftMargin;
                lp.topMargin = decorWindow.topMargin;
                lp.rightMargin = decorWindow.rightMargin;
                lp.bottomMargin = decorWindow.bottomMargin;
            }
            decorWindow.showAnimator = showAnimator;
            decorWindow.hideAnimator = hideAnimator;
            decorWindow.showAni = showAni;
            decorWindow.hideAni = hideAni;
            decorWindow.animatesss = animatesss;
            return decorWindow;
        }

        public Builder fitStatusbar(boolean fit) {
            this.fitStatusBar = fit;
            return this;
        }

        public Builder bindClick(int resource, final View.OnClickListener listener) {
            if (this.view == null)
                throw new IllegalStateException("setView() must be called before bindClick()!");
            this.view.findViewById(resource).setOnClickListener(listener);
            return this;
        }

        public Builder animator(Animatesss animatesss) {
            this.animatesss = animatesss;
            return this;
        }

        public Builder animated(boolean animate) {
            this.showAni = animate;
            this.hideAni = animate;
            return this;
        }
    }


    private View realView() {
        return focus ? ((ViewGroup) view).getChildAt(0) : view;
    }

    public void setLocation(PathPoint p) {
        realView().setTranslationX(p.x);
        realView().setTranslationY(p.y);
    }

    /**
     * @param mContext
     * @return
     */
    public static int getStatusBarHeight(Context mContext) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 60;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = mContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
