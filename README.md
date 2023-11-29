# Keyboardhidedemo
android 配合scrollview 解决键盘遮挡EditText的问题
当触摸EditText ,键盘弹出-> 

activity窗口的大小会发生改变->

 //在根部局添加layout大小发生改变监听器: getContentView().addOnLayoutChangeListener(this);

在onLayoutChange 里计算当前拥有焦点的editText的位置，计算scrollView 需要滑动多少距离才能使EditText显示在视图上，然后进行滑动；

文章链接：https://blog.csdn.net/ange_li/article/details/81036056
