# Countries
Countries is simple country info app. It shows data about countries.



## Architecture
This app implements MVVM architecture. App consist of different fragments and 1 root activity. Activity holds a container layout in order to manage fragments which will be controlled by navigation component. 

User can see countries inside HomePageFragment
User can see saved countries inside SavedFragment
User can see details of the country inside DetailFragment
User can delete country by swiping
User can add country to saved list by clicking star icon
User can remove country from saved list by clicking star icon



## Tech Stack
* Kotlin
* Coroutines
* MVVM Architecture
* Realm
* Navigation Component
* View Binding
* RecyclerView
* Shared Preferences
* Retrofit
* Coil
