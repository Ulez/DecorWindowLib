# DecorWindowLib
以View的形式实现的类似PopupWindow与dialog的效果。

##usage

```java
decorWindowFocus = DecorWindow.Builder
                    .newBuilder(this)
                    .setView(dialogView, color)
//                    .setPosPer(0.01, 0.01, 0.99, 0.99) //set position by percent;
                    .setPosDp(50, 50, 358, 638)    //set position by dp;
                    .bindClick(R.id.button, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            .......
                        }
                    })                
                    .animate(new DecorWindow.Animatesss() {

                        @Override
                        public Animator getShowAnimator(View view) {
                            return ObjectAnimator.ofFloat(view, "translationX", view.getWidth(), 0).setDuration(600);
                        }

                        @Override
                        public Animator getHideAnimator(View view) {
                            return ObjectAnimator.ofFloat(view, "translationX", 0, view.getWidth()).setDuration(600);
                        }
                    })
                    .build()
                    .showOrHide();
```
