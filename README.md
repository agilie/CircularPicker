# CircularPicker
We are pleased to offer you our new free lightweight plugin named CircularPicker.

CircularPicker is helpful for creating a controller aimed to manage any calculated parameter. For example, it can be used as a countdown timer or for keeping the score in the game interface.

CircularPicker can be customized to meet your individual requirements. The developer can set the number of the controllers and their design by selecting a color, gradient and other similar parameters. In addition, it’s possible to specify the transition type for showing controllers on the screen.

## Example
To run the example project, clone the repo and run [sample](TimePickerExample/).

### How does it work?

Just add CircularPickerPagerContainer which contains CircularPickerViewPager to your layout file.
````xml
<com.agilie.circularpicker.ui.view.CircularPickerPagerContainer
        android:id="@+id/timePickerPagerContainer"
        android:layout_width="match_parent"
        android:layout_height="350dp">

        <com.agilie.circularpicker.ui.view.CircularPickerViewPager
            android:id="@+id/view_pager"
            android:layout_width="300dp"
            android:layout_height="match_parent"
            android:layout_centerHorizontal="true"
            android:layout_gravity="center_horizontal">

        </com.agilie.circularpicker.ui.view.CircularPickerViewPager>
</com.agilie.circularpicker.ui.view.CircularPickerPagerContainer>
    
   
````
Also you can use only CircularPickerView
````xml
<com.agilie.circularpicker.ui.view.CircularPickerView
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
````

The library contains three key elements: 
1. CircularPickerPagerContainer - a custom container to show more than one page at a time.
2. CircularPickerViewPager - a custom ViewPager in which we define the region in the view element for swipe action.
3. CircularPickerView - a controller aimed to manage any calculated parameter.</br>
CircularPickerView has the following settings:
````kotlin
var colors : intArrayOf
var gradientAngle : Int 
var maxLapCount : Int           // number of laps (required)
var maxValue : Int              // total values (required)
var currentValue : Int 
var maxPullUp: Float 
var viewSpace : Float 
var centeredTextSize : Float 
var centeredText : String 
var centeredTextColor: Int
var centeredTypeFace : TypeFace
var valueChangeListener : object
var colorChangeListener : object
````

### Our example of using CircularPicker
Let's see how we can use it in practice.
In our *layout.xml* we added CircularPickerPagerContainer which contains CircularPickerViewPager, then created CircularPickerView in the Activity and set up parameters:

````gradle
СircularPickerView(context).apply {
            colors = (intArrayOf(
                    Color.parseColor("#00EDE9"),
                    Color.parseColor("#0087D9"),
                    Color.parseColor("#8A1CC3")))
            gradientAngle = 220
            maxLapCount = 2
            currentValue = 13
            maxValue = 24
            centeredTextSize = 60f
            centeredText = "Hours"
 ````
Here are also two callback interfaces provided with _CircularPickerView_. Use them to handle changes made during the interaction with the component: 

````kotlin
interface ValueChangedListener {
    fun onValueChanged(value: Int)
}

interface ColorChangedListener {
    fun onColorChanged(r: Int, g: Int, b: Int)
}
````

## Usage

### Gradle

Add dependency in your `build.gradle` file:
````gradle
TODO
````

### Maven
Add rependency in your `.pom` file:
````xml
<dependency>
  <groupId>com.agilie</groupId>
  <artifactId>circular-picker</artifactId>
  <version>1.0</version>
  <type>pom</type>
</dependency>
````

## Requirements

CircularPicker works on Android API 16+

## Troubleshooting

Problems? Check the [Issues](https://github.com/agilie/AGMobileGift/issues) block
to find the solution or create an new issue that we will fix asap.


## Author

This library is open-sourced by [Agilie Team](https://www.agilie.com) <info@agilie.com>

## Contributors

- [Eugene Surkov](https://github.com/ukevgen)
- [Roman Kapshuk](https://github.com/RomanKapshuk)

## Contact us
If you have any questions, suggestions or just need a help with web or mobile development, please email us at<br/> <android@agilie.com><br/>
You can ask us anything from basic to complex questions. <br/>
We will continue publishing new open-source projects. Stay with us, more updates will follow!<br/>

## License

The [MIT](LICENSE.md) License (MIT) Copyright © 2017 [Agilie Team](https://www.agilie.com)
