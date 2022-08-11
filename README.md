# Countries
Countries is simple country info app. It shows data about countries.

# Screenshots
![countries_ss_1](https://user-images.githubusercontent.com/62806425/184225306-afd1229b-e521-4833-a4ce-5516af0c9462.png)
![countries_ss_2](https://user-images.githubusercontent.com/62806425/184225931-b911bd90-07c3-44b9-8870-3dd31c8e67cb.png)
![countries_ss_3](https://user-images.githubusercontent.com/62806425/184225949-daa3b025-1829-4a9b-bf6a-2374d6d77037.png)



## Architecture
This app implements MVVM architecture. App consist of different fragments and 1 root activity. Activity holds a container layout in order to manage fragments which will be controlled by navigation component. 

<p>User can see countries inside HomePageFragment</p>
<p>User can see saved countries inside SavedFragment</p>
<p>User can see details of the country inside DetailFragment</p>
<p>User can delete country by swiping</p>
<p>User can add country to saved list by clicking star icon</p>
<p>User can remove country from saved list by clicking star icon</p>




## Tech Stack
* Kotlin
* Coroutines
* MVVM Architecture
* Realm
* Navigation Component
* View Binding
* Navigation Component
* RecyclerView
* Shared Preferences
* Retrofit
* Coil
