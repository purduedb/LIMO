#limo module

from com.ziclix.python.sql import zxJDBC
from com.vividsolutions.jts.geom import Coordinate
from com.vividsolutions.jts.geom import GeometryFactory
from com.vividsolutions.jts.geom import Geometry
import math

#Constants
IP = "10.211.55.9"
PORT = "5432"
DB_USER = "postgres"
DB = "gisDBTest"
PASSWORD = "''"
CITY = ""
STATE = ""
ZIP = ""
CONNECT_STRING = "jdbc:postgresql://" + IP + ":" + PORT + "/" + DB  

def test():
        return "say something"

def setCity(c):
        CITY = c

def setState(s):
        STATE = s

def setZipCode(z):
        ZIP = z

#prompts the user to input the address
def read_address(street, city, state, zipcode):
        address = street + "," + city + "," + state + "," + zipcode 
        return address

#prompts the user to input the address as intersection
def read_intersection(street1, street2, city, state, zipcode):
        address = "intersection," + street1 + "," + street2 + "," +  state + "," + city + "," + zipcode 
        return address

# adds a marker on a address by creating a row in visualize file: MARKER,lon,lat
# returns the geolocation of the marker
def display_marker (address):

        if type(address) is str:
                geolocation = geocode_address(address)
        else:
                #the address is a list [lon, lat]
                geolocation = address
	instruction = "MARKER,"+ str(geolocation[0]) + "," + str(geolocation[1]) + "\n"
	f = open("visualize.txt", 'a')
	f.write(instruction)
	f.close()

	return geolocation

# displays a message on address by making a row in visualize file: MSG,message,lon,lat
def display_message (message, address):
	geolocation = geocode_address(address)
	instruction = "MSG,"+ message + "," + str(geolocation[0]) + "," + str(geolocation[1]) + "\n"
	f = open("visualize.txt", 'a')
	f.write(instruction)
	f.close()

#returns [lon,lat] of address        
def geocode_address(address):
        query = "";
        #check whether the address represents an intersection
        x = address.find("intersection,")
   
        if x == 0:
                address = address [13:]
                addressList = address.split(',')
                for i in range(len(addressList)):
                        addressList[i] = "'"+addressList[i]+"'"
                #print addressList
                query =  "SELECT ST_AsText(geomout) FROM geocode_intersection(" + addressList[0] + "," +  addressList[1] + "," + addressList[2] + "," + addressList[3] + "," + addressList[4]+",1)"
        else:
                address = "'"+address+"'"
                query =  "SELECT ST_AsText(geomout) FROM geocode(" + address + ",1)"

        db = zxJDBC.connect(CONNECT_STRING, "postgres", "", "org.postgresql.Driver")
        c = db.cursor()
        c.execute(query)
        rowcount = c.rowcount

        if rowcount == 1:
                row = c.fetchone()
        
        c.close()
        db.close()

        if rowcount == 1:
                return extract_point(str(row))
        else:
                return "NULL"


#extract the point from the geocode string using ST_AsText(geomout): (u'POINT(-86.882223 40.423635)',)
def extract_point(geo_output):
	geo_output = geo_output[3:len(geo_output)]
	start = geo_output.find('(')
	end = geo_output.find(')')
	geo_output = geo_output[start+1:end]
	
	lonLat = geo_output.split(' ')
	lonLat[0] = eval(lonLat[0])
	lonLat[1] = eval(lonLat[1])
	
	return lonLat

#extract polygon points from geo_output string using ST_AsText(ST_Simplify(the_geom, 0.05)): (u'MULTIPOLYGON((( geo_point1, ... )))',)
def extract_polygon(geo_output):

        end = geo_output.find(')')
        geo_output = geo_output[18:end]

        geoList = geo_output.split(',')
        outputList = []
        outputList.append("POLYGON")
        for i in range(len(geoList)):
                geoSubList = geoList[i].split(" ")
                outputList.append(geoSubList)
                
        return outputList

#extract polyline points from geo_output string: (u'MULTILINESTRING((  geo_point1, ... ))',)
def extract_polyline(geo_output):
        end = geo_output.find(')')
        geo_output = geo_output[20:end]

        geoList = geo_output.split(',')
        outputList = []
        outputList.append("POLYLINE")
        for i in range(len(geoList)):
                geoSubList = geoList[i].split(" ")
                outputList.append(geoSubList)
                
        return outputList


# sets the commuters start location and returns [lon,lat]
# also sets the global variables: CITY, STATE, and ZIP associated with commuter
def start_at(address):
        x = address.find("intersection,")

        if x == 0:
                addr = address [13:]
                addressList = addr.split(',')
                global CITY
                CITY = addressList[3]
                global STATE
                STATE = addressList[2]
                global ZIP
                ZIP = addressList[4]

        else:
                addressList = address.split(',')
                global CITY
                CITY = addressList[1]
                global STATE
                STATE = addressList[2]
                global ZIP
                ZIP = addressList[3]
                
        geolocation = geocode_address(address)
        return geolocation


# returns the distnace between two geocoordinates
#def compute_distance(geoloc1, geoloc2):
#	distance = 0.0
#	retuen distance
	
# returns the geooaction after movement
def move_on_until(street1, street2):     
        return geocode_intersection(street1, street2)

def geocode_intersection(street1, street2):
        query =  "SELECT ST_AsText(geomout) FROM geocode_intersection('" + street1 + "','" + street2 + "','" + STATE + "','" + CITY + "','" + ZIP+"',1)"

        db = zxJDBC.connect(CONNECT_STRING, "postgres", "", "org.postgresql.Driver")
        c = db.cursor()
        c.execute(query)
        rowcount = c.rowcount

        if rowcount == 1:
                row = c.fetchone()
        
        c.close()
        db.close()

        if rowcount == 1:
                return extract_point(str(row))
        else:
                return "NULL"
        


def show_on_map(geoList):
        db = zxJDBC.connect(CONNECT_STRING, "postgres", "", "org.postgresql.Driver")
        
        instruction = "POLYLINE,"
        for i in range(len(geoList)):
                instruction += str(geoList[i][0]) + ';' + str(geoList[i][1])+ ','
                
        instruction = instruction [: len(instruction)-1]
        instruction += "\n"
        f = open("visualize.txt", 'a')
	f.write(instruction)
	f.close()

def get(name, description, geomType):
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        c = db.cursor()
        if geomType == "POLYGON":
                if description == "STATE":
                        query = "SELECT ST_AsText(ST_Simplify(the_geom, 0.05)) FROM tiger_data.state_all where name = '" + name +"'"
                        c.execute(query)
                        rowcount = c.rowcount

                        if rowcount == 1:
                                row = c.fetchone()
                                c.close()
                                db.close()
                                return extract_polygon(str(row))

        elif geomType == "POINT":
                if description == "STATE":
                        query = "SELECT intptlon, intptlat from tiger_data.state_all where name = '" + name +"'"
                        c.execute(query)
                        rowcount = c.rowcount
                        if rowcount == 1:
                                row = c.fetchone()
                                c.close()
                                db.close()
                                lonlat = []
                                lonlat.append(eval(row[0]))
                                lonlat.append(eval(row[1]))
                                
                                return lonlat

        elif geomType == "POLYLINE":
                if description == "RIVER":
                        query = "Select ST_AsText(the_geom) from tiger_data.in_linearwater where fullname = '" + name + "'"
                        c.execute(query)
                        rowcount = c.rowcount
                        if rowcount >1:
                                row = c.fetchone()
                                c.close()
                                db.close()
                                return extract_polyline(str(row))
                        
                
        return "NULL"

def get_all(description, geomType):
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        c = db.cursor()
        if description == "STATE":
                if geomType == "POLYGON":
                        query = "SELECT ST_AsText(ST_Simplify(the_geom, 0.05)) FROM tiger_data.state_all" 
                        c.execute(query)
                        rowcount = c.rowcount

                        if rowcount > 1:
                                lonlatList = []
                                for i in range (rowcount):
                                        row = c.fetchone()      
                                        lonlatList.append(extract_polygon(str(row)))
                                c.close()
                                db.close()
                                return lonlatList

                elif geomType == "POINT":
                        query = "SELECT intptlon, intptlat from tiger_data.state_all"
                        c.execute(query)
                        rowcount = c.rowcount
                        if rowcount > 1:
                                lonlatList = []
                                for i in range (rowcount):
                                        row = c.fetchone()
                                        lonlat = []
                                        lonlat.append(eval(row[0]))
                                        lonlat.append(eval(row[1]))
                                        lonlatList.append(lonlat)
                                c.close()
                                db.close()
                                return lonlatList
                        
                return "NULL"



def display_shape(shape):
        instruction = create_geostring(shape)
        f = open("visualize.txt", 'a')
        f.write(instruction)
        f.close()

#creates a string of geocoordinates: lon1;lat,lon;lat,...
def create_geostring(geoList):
        instruction = geoList[0] + ","
        for i in range(1,len(geoList)):
                instruction += str(geoList[i][0]) + ';' + str(geoList[i][1])+ ','
                
        instruction = instruction [: len(instruction)-1]
        instruction += "\n"
        return instruction

#create a coordinate list [ Coor(lon1 lat1), Coor(lon1 lat1)] in order to compy with the JTS library format
def create_coorList(geoList):
        coords = []
        for i in range(1,len(geoList)):
                c = Coordinate(float(geoList[i][0]), float(geoList[i][1]))
                coords.append(c)

        return coords

#creates a geometry type from a list of coordinates
def create_polygon(coord_list):
        geomfactx = GeometryFactory()
        ring = geomfactx.createLinearRing(coord_list)
        hole = []
        polygon = geomfactx.createPolygon(ring, hole)
        return polygon

def create_shape(geoList):
        if geoList[0] == "POLYGON":
                coord_list = create_coorList(geoList)
                shape = create_polygon(coord_list)
                return shape
        return "NULL"

# returns true if shape1 touches shape2
def touches(geoList1, geoList2):
        shape1 = create_shape(geoList1)
        shape2 = create_shape(geoList2)

        if shape1.touches(shape2):
                return True
        return False

# returns true if shape1 intersects shape2
def intersects(geoList1, geoList2):
        shape1 = create_shape(geoList1)
        shape2 = create_shape(geoList2)

        if shape1.intersects(shape2):
                return True
        return False

# returns true if shape1 overlaps shape2
def overlaps(geoList1, geoList2):
        shape1 = create_shape(geoList1)
        shape2 = create_shape(geoList2)

        if shape1.overlaps(shape2):
                return True
        return False
        
# returns true if shape1 contains shape2
def contains(geoList1, geoList2):
        shape1 = create_shape(geoList1)
        shape2 = create_shape(geoList2)

        if shape1.contains(shape2):
                return True
        return False
                
def calculate_distance(geo1, geo2):
        return distFrom(geo1[1], geo1[0], geo2[1], geo2[0])

#compute the distance between two geo_coordinates using the Great Circle Distance formula GCD
def distFrom(lat1, lng1, lat2, lng2):
	    earthRadius = 3958.75
	    dLat = math.radians(lat2-lat1)
	    dLng = math.radians(lng2-lng1)
	    a = math.sin(dLat/2) * math.sin(dLat/2) + math.cos(math.radians(lat1)) * math.cos(math.radians(lat2)) * math.sin(dLng/2) * math.sin(dLng/2)
	    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1-a))
	    return   earthRadius * c

def caculate_distance_commuted(commuter):
        result = 0
        for i in range(len(commuter)-1):
                result = result + calculate_distance(commuter[i], commuter[i+1])
        return round(result,2)
