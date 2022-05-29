# Solar system - Web Service
**It is a web service use to realize a personalized interface towards another web service used to get open date about planets in solar system
Just a _school project_**

# WEB API
## Data source : [https://api.le-systeme-solaire.net/en/](https://api.le-systeme-solaire.net/en/)
## 1. Return of information of a body given its name

   * #### Method : GET
   * #### Request :
     - URL -> http://.../solarsystem/body?id=...
     
   * Response:
     - Status code : `200`
 
     ### Body :
```xml
   <body>
      <name>...</name>
      <mass>...</mass>
      <vol>...</vol>
      <density>...</density>
      <gravity>...</gravity>
      <bodyType>...</bodyType>
      <orbitPeriod>...</orbitPeriod>
      <moons>
      <moonUrl name="...">...</moonUrl>
      ...
      </moons>
   </body>    
```

### Example :

* ### URL -> http://.../solarsystem/body?id=Lempo
```xml
<body>
    <name>(47171) Lempo</name>
    <mass>null</mass>
    <vol>null</vol>
    <density>1.0</density>
    <gravity>0.0</gravity>
    <bodyType>Asteroid</bodyType>
    <orbitPeriod>91678.0</orbitPeriod>
    <moons>null</moons>
</body>
```
   * Response data :
   - `name` -> english name of body
   - `mass` -> Boby mass in 10^n kg.
   - `vol` ->  Body volume in 10^n kg^3.
   - `density` -> Body density in g/cm^3.
   - `gravity` -> Surface gravity in m/s^2.
   - `bodyType` -> The body type :  for examples Star, Planet, Dwarf Planet, Asteroid ...
   - `moons` -> List of all moons : moonUrl -> url used to request data about the moon

   * Errors:
     * **Status code** : `400`  and error description
   -----
## 2. List of bodies of a specific body type
 * #### Method : GET
 * #### Request :
   - URL -> http://.../solarsystem/list?type=...

   * #### Response:
     - Status code : `200`
   ### Body :
```xml
<bodies>
    <bodyType>...</bodyType>
    
    <body>
        <name>...</name>
        <mass>...</mass>
        <vol>...</vol>
        <density>...</density>
        <gravity>...</gravity>
        <orbitPeriod>...</orbitPeriod>
        <moons>
            <moonUrl name="...">...</moonUrl>
            ...
        </moons>
    </body>
    <body>
        ...
    </body>
    ...
</bodies>
```

### Example :

* ### URL -> http://.../solarsystem/list?type=asteroid
```xml

<bodies>
    <bodyType>Asteroid</bodyType>
    
    <body>
        <name>(1) Cérès</name>
        <mass>9.3930010^20</mass>
        <vol>4.2100010^9</vol>
        <density>2.161</density>
        <gravity>0.28</gravity>
        <bodyType>Asteroid</bodyType>
        <orbitPeriod>1681.63</orbitPeriod>
        <moons>null</moons>
    </body>
    
    <body>
        <name>(6) Hébé</name>
        <mass>6.7000010^20</mass>
        <vol>null</vol>
        <density>1.0</density>
        <gravity>0.0</gravity>
        <bodyType>Asteroid</bodyType>
        <orbitPeriod>1380.373</orbitPeriod>
        <moons>null</moons>
    </body>
    
    <body>
        ...
    </body>
</bodies>
```
* Response data :
- `name` -> english name of body
- `mass` -> Boby mass in 10^n kg.
- `vol` ->  Body volume in 10^n kg^3.
- `density` -> Body density in g/cm^3.
- `gravity` -> Surface gravity in m/s^2.
- `bodyType` -> The body type :  for examples Star, Planet, Dwarf Planet, Asteroid ...
- `moons` -> List of all moons : moonUrl -> url used to request data about the moon

* Errors:
    * **Status code** : `400`  and error description

