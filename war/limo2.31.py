#limo module

from com.ziclix.python.sql import zxJDBC
from com.vividsolutions.jts.geom import Coordinate
from com.vividsolutions.jts.geom import GeometryFactory
from com.vividsolutions.jts.geom import Geometry
from com.vividsolutions.jts.geom import LineSegment
from com.vividsolutions.jts.geom import Envelope

from operator import itemgetter
from example.edu.server import ExampleServiceImpl

import math
from copy import deepcopy

earth_radius = 3960.0
degrees_to_radians = math.pi/180.0
radians_to_degrees = 180.0/math.pi

#Flag that is set to True when the movement type is distance
last_move = False

#Constants
#IP = "192.168.126.140"
IP = "ibnkhaldun.cs.purdue.edu"
PORT = "5439"
#DB_USER = "postgres"
DB_USER = "limo"
#DB = "gisDBTest"
DB = "gisdb3"
#PASSWORD = "''"
PASSWORD = "limo"
CITY = ""
STATE = ""
ZIP = ""
CONNECT_STRING = "jdbc:postgresql://" + IP + ":" + PORT + "/" + DB

#globals
#bearing = 0
commuters = dict()
debug = 0

depth = 0
def find_all_roads_within(commuterName, currentLength, maxLength, coveredRoads):#, previousCommuterName):

    global depth
    print "==================================================== DEPTH ",
    print depth
    depth += 1

    # if depth > 1:
    #     return 0

    # get how many roads / ways I have

    roadNames = get_road_names(get_current_point(str(commuterName)))
    print "all road names : ",
    print roadNames

    point = commuters.get(str(commuterName))[1]
    print "current point : ",
    print point

    global ZIP


    print "roadnames before : ",
    print roadNames
    for cr in coveredRoads:
        if cr in roadNames:
            roadNames.remove(cr)
    print "roadnames after : ",
    print roadNames
    

    # nextPoint = move_along_street(point[0], point[1], roadNames[0], ZIP, commuters.get(str(commuterName))[3], 0.01)    
    # print "next point : ",
    # print nextPoint

    # cnt = 0
    # global depth
    # if depth > 2:
    #     return 0
    # else:
    #     depth += 1


    for i in range(len(roadNames)):
        
        print "current road : ",
        print roadNames[i]

        newCoveredRoads = deepcopy(coveredRoads)            
        newCoveredRoads.append(str(roadNames[i]))


        nextPoint = move_along_street(point[0], point[1], roadNames[i], ZIP, commuters.get(str(commuterName))[3], 0.01)    
        print "nextPoint : "
        print nextPoint
        

        intersections1 = []
        intersections2 = []

        if nextPoint[3][0] != 0.0:

            print "came in 2 - 1"
            newName = str(commuterName) + "_" + roadNames[i] + "-1"
            copy_commuter(commuterName, newName)
            
            
            
            cnt = 0
            stop = False
            while stop == False:
                
                # if cnt > 5:
                #     break

                if cnt == 0:
                    nextBearings = turn_to(newName, roadNames[i], nextPoint[3][0])    
                else:
                    nextBearings = turn_to(newName, roadNames[i])    

                if nextBearings[0] == 0.0: # end of road
                    print "endofstraightraod"
                    break
                else:
                    move_to_next_intersection(newName)
                    # nextBearings = turn_to(newName, roadNames[i])
                      
                    names = get_road_names(get_current_point(newName))

                    
                    for name in names:
                        if name not in newCoveredRoads:

                            nextCommuterName = str(newName) + "-" + str(cnt)
                            copy_commuter(newName, nextCommuterName)

                            moveSoFar = display_distance(nextCommuterName)

                            print "moveSoFar : ",
                            print moveSoFar

                            intersections1.append([name, nextCommuterName, commuterName, moveSoFar])

                            if moveSoFar < maxLength:    
                                cnt += 1
                            else:
                                stop = True

                                # intersections2[len(intersections2)-1][0] = intersections2[len(intersections2)-2][0]
                                # intersections2[len(intersections2)-1][1] = intersections2[len(intersections2)-2][1]
                                # intersections2[len(intersections2)-1][2] = intersections2[len(intersections2)-2][2]
                                # intersections1.pop()
                                break

                
                    
            print "intersections1 : ",
            print intersections1


        

        if nextPoint[3][1] != 0.0:
            print "came in 2 - 2"
            newName = str(commuterName) + "_" + roadNames[i] + "-2"
            copy_commuter(commuterName, newName)

            cnt = 0
            stop = False
            # previous_name = "";
            while stop == False:
                
                if cnt == 0:
                    nextBearings = turn_to(newName, roadNames[i], nextPoint[3][1])    
                    # previous_name = newName
                else:
                    nextBearings = turn_to(newName, roadNames[i])    

                if nextBearings[0] == 0.0: # end of road
                    print "endofstraightraod"


                    break
                else:
                    move_to_next_intersection(newName)
                    # nextBearings = turn_to(newName, roadNames[i])
                      
                    names = get_road_names(get_current_point(newName))

                    
                    for name in names:
                        if name not in newCoveredRoads:

                            nextCommuterName = str(newName) + "-" + str(cnt)
                            copy_commuter(newName, nextCommuterName)

                            moveSoFar = display_distance(nextCommuterName)

                            print "moveSoFar : ",
                            print moveSoFar

                            intersections2.append([name, nextCommuterName, commuterName, moveSoFar])

                            if moveSoFar < maxLength:    
                                cnt += 1
                            else:
                                # intersections2.append([name, nextCommuterName, get_current_point(newName), moveSoFar])
                                stop = True

                                # intersections2[len(intersections2)-1][0] = intersections2[len(intersections2)-2][0]
                                # intersections2[len(intersections2)-1][1] = intersections2[len(intersections2)-2][1]
                                # intersections2[len(intersections2)-1][2] = intersections2[len(intersections2)-2][2]
                                # intersections2[len(intersections2)-1][3] 
                                # intersections2.pop()
                                break
                                # need to go last remain lenght
                    
            print "intersections2 : ",
            print intersections2





        

        num = 0
        for intersection in intersections1:
            num += 1

            if intersection[3] < maxLength:
                find_all_roads_within(intersection[1], intersection[3], maxLength, newCoveredRoads)
            else:
                print "showonmap1-L on ==="
                # print intersection[0]
                # print commuters.get(str(intersection[1]))[2]
                # lastLength = maxLength - display_distance(str(intersection[2]))
                # print "length : ",
                # print lastLength
                lastCommuterName = str(intersection[1]) + "-L"
                copy_commuter(str(intersection[1]), lastCommuterName)     

                # lastLength = maxLength - display_distance(lastCommuterName)
                # turn_to(lastCommuterName, str(intersection[0]))

                rollback_commuter(lastCommuterName)

                lastLength = maxLength - display_distance(lastCommuterName)

                print "length : ",
                print lastLength

                show_commuter(lastCommuterName)

                move_distance(lastCommuterName, lastLength)

                show_on_map(lastCommuterName)


        num = 0
        for intersection in intersections2:
            num += 1

            if intersection[3] < maxLength:
                find_all_roads_within(intersection[1], intersection[3], maxLength, newCoveredRoads)
            else:
                print "showonmap2-L on ==="
                # print intersection[0]
                # print commuters.get(str(intersection[1]))[2]
                # lastLength = maxLength - display_distance(str(intersection[2]))
            
                lastCommuterName = str(intersection[1]) + "-L"

                copy_commuter(str(intersection[1]), lastCommuterName)        
                
                rollback_commuter(lastCommuterName)

                lastLength = maxLength - display_distance(lastCommuterName)

                print "length : ",
                print lastLength
                
                show_commuter(lastCommuterName)

                move_distance(lastCommuterName, lastLength)

                show_on_map(lastCommuterName)


       

    
def cover_all_roads_within(commuterName, length):
    coveredRoads = []
    # get current road name based on the point

    point = commuters.get(str(commuterName))[1]
    print "point ",
    print point

    newCommuter = copy_commuter(str(commuterName), str(commuterName)+"_1")
    cnt = 1

    find_all_roads_within(str(commuterName)+"_1", 0, length, coveredRoads)#, str(commuterName))


def rollback_commuter(commuterName):
    commuters.get(str(commuterName))[0] = commuters.get(str(commuterName))[4][0]
    commuters.get(str(commuterName))[1] = commuters.get(str(commuterName))[4][1]
    commuters.get(str(commuterName))[2] = commuters.get(str(commuterName))[4][2]
    commuters.get(str(commuterName))[3] = commuters.get(str(commuterName))[4][3]
    




def start_at(commuterName, address = None, direction = 0):
    global commuters
    x = address.find("intersection,")
    addressList = []

    if x == 0:
        addr = address [13:]
        addressList = addr.split(',')
        # print addressList

        for i in range(len(addressList)):
            addressList[i] = addressList[i].rstrip()
            addressList[i] = addressList[i].lstrip()

        global CITY
        CITY = addressList[3]
        global STATE
        STATE = addressList[2]
        global ZIP
        ZIP = addressList[4]

    else:
        addressList = address.split(',')
        
        for i in range(len(addressList)):
            addressList[i] = addressList[i].rstrip()
            addressList[i] = addressList[i].lstrip()
            
        global CITY
        CITY = addressList[1]
        global STATE
        STATE = addressList[2]
        global ZIP
        ZIP = addressList[3]
        
    geolocation = geocode_address(address)
    

    # commuter = [set of Points / current point / current st. name / direction]

    # get only st. name
    commuterName = str(commuterName)
    stName = addressList[0]
    items = stName.split(" ")

    stName = ""
    for i in range(1,len(items)):
        stName += items[i] + " "

    stName = stName[0:-1]
    # print stName
    # get direction (bearing)
    if str(direction).isdigit():
        direction = int(direction)
    else:
        direction = orient_to(commuterName, direction)

    # set commuter
    if str(commuterName) in commuters:
        commuters.get(commuterName)[0].append(geolocation)
        commuters.get(commuterName)[1] = geolocation
        commuters.get(commuterName)[2] = stName
        commuters.get(commuterName)[3] = direction
        commuters.get(commuterName)[4] = []
    else:
        commuters[commuterName] = [[], "", "", 0, []]
        commuters.get(commuterName)[0].append(geolocation)
        commuters.get(commuterName)[1] = geolocation
        commuters.get(commuterName)[2] = stName
        commuters.get(commuterName)[3] = direction
        commuters.get(commuterName)[4] = []


    return geolocation
    # commuter = [set of Points / current point / current st. name / direction]
    


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
    instruction = "MARKER," + str(geolocation[0]) + "," + str(geolocation[1]) + "\n"
    ExampleServiceImpl.setResultString(instruction)

    # f = open("visualize.txt", 'a')
    # f.write(instruction)
    # f.close()
    return geolocation

# displays a message on address by making a row in visualize file: MSG,message,lon,lat
def display_message (message, address):
    
    if type(address) is str: 
        geolocation = geocode_address(address)
        #print geolocation
    else:
        geolocation = address

    instruction = "MSG," + message + "," + str(geolocation[0]) + "," + str(geolocation[1]) + "\n"       

    # f = open("visualize.txt", 'a')
    # f.write(instruction)
    # f.close()
    ExampleServiceImpl.setResultString(instruction) 


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

        
        #db = zxJDBC.connect(CONNECT_STRING, "postgres", "", "org.postgresql.Driver")
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        
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
                raise Exception("Geocode " + address + " returned NULL")
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

#extract the point from the geocode string using ST_AsText(geomout): POINT(-86.882223 40.423635)
def extract_point_v2(geo_output):
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

#extract polygon points from geo_output string using ST_AsText(ST_Simplify(the_geom, 0.05)): (u'POLYGON((( geo_point1, ... )))',)
def extract_polygon_v2(geo_output):

        end = geo_output.find(')')
        geo_output = geo_output[12:end]

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


#extract polyline points from geo_output string: LINESTRING((  geo_point1, ... ))',
def extract_line_string(geo_output):
        end = geo_output.find(')')
        geo_output = geo_output[14:end]

        geoList = geo_output.split(',')
        outputList = []
        outputList.append("POLYLINE")
        for i in range(len(geoList)):
                geoSubList = geoList[i].split(" ")
                outputList.append(geoSubList)
                
        return outputList

#extract polyline points from geo_output string: LINESTRING((  geo_point1, ... ))',
def extract_line_string2(geo_output):
        end = geo_output.find(')')
        geo_output = geo_output[11:end]

        geoList = geo_output.split(',')
        outputList = []
        outputList.append("POLYLINE")
        for i in range(len(geoList)):
                geoSubList = geoList[i].split(" ")
                outputList.append(geoSubList)
                
        return outputList

def copy_commuter(commuterName, copyName):
    commuters[str(copyName)] = deepcopy(commuters.get(str(commuterName)))
    return commuters[str(copyName)]

def show_commuter(commuterName):
    print "Name: " + str(commuterName)

    print "Path: ",
    print commuters.get(str(commuterName))[0]

    print "Last Point: ",
    print commuters.get(str(commuterName))[1]

    print "Current Road Name: ",
    print commuters.get(str(commuterName))[2]

    print "Bearing: ",
    print commuters.get(str(commuterName))[3]

# sets the commuters start location and returns [lon,lat]
# also sets the global variables: CITY, STATE, and ZIP associated with commuter
# def start_at(commuterName, address):
#         global commuters
#         x = address.find("intersection,")
#         addressList = []

#         if x == 0:
#                 addr = address [13:]
#                 addressList = addr.split(',')
#                 global CITY
#                 CITY = addressList[3]
#                 global STATE
#                 STATE = addressList[2]
#                 global ZIP
#                 ZIP = addressList[4]

#         else:
#                 addressList = address.split(',')
#                 global CITY
#                 CITY = addressList[1]
#                 global STATE
#                 STATE = addressList[2]
#                 global ZIP
#                 ZIP = addressList[3]
                
#         geolocation = geocode_address(address)
#         #commuter_points.append(geolocation)
#         #commuter.append(geolocation)

#         # commuter = [set of Points / current point / current st. name / direction]

        
#         commuterName = str(commuterName)
#         stName = addressList[0]
#         items = stName.split(" ")

#         stName = ""
#         for i in (1,len(items)-1):
#             stName += items[i] + " "

#         stName = stName[0:-1]


#         if str(commuterName) in commuters:
#             commuters.get(commuterName)[0].append(geolocation)
#             commuters.get(commuterName)[1] = geolocation
#             commuters.get(commuterName)[2] = stName
#             commuters.get(commuterName)[3] = 0
#             commuters.get(commuterName)[4] = []
#         else:
#             commuters[commuterName] = [[], "", "", 0, []]
#             commuters.get(commuterName)[0].append(geolocation)
#             commuters.get(commuterName)[1] = geolocation
#             commuters.get(commuterName)[2] = stName
#             commuters.get(commuterName)[3] = 0
#             commuters.get(commuterName)[4] = []

#         if debug:
#             print "start_at() : "
#             print commuters.get(commuterName)
#         ########################################################################################

#         return geolocation
    
# sets the commuters start location and returns [lon,lat]
# also sets the global variables: CITY, STATE, and ZIP associated with commuter
def get_location(address):
        geolocation = geocode_address(address)
        return geolocation


# returns the distnace between two geocoordinates
#def compute_distance(geoloc1, geoloc2):
#    distance = 0.0
#    retuen distance

def move_until(commuterName, street2):

    commuter = commuters.get(str(commuterName))[0]
    street1 = commuters.get(str(commuterName))[2]
    currentPoint = commuters.get(str(commuterName))[1]

    commuters.get(str(commuterName))[4] = []
    tmp = deepcopy(commuters.get(str(commuterName))[0])
    commuters.get(str(commuterName))[4].append(tmp)

    tmp = deepcopy(commuters.get(str(commuterName))[1])
    commuters.get(str(commuterName))[4].append(tmp)

    tmp = deepcopy(commuters.get(str(commuterName))[2])
    commuters.get(str(commuterName))[4].append(tmp)

    tmp = deepcopy(commuters.get(str(commuterName))[3])
    commuters.get(str(commuterName))[4].append(tmp)

    


    lonlat = geocode_intersection2(street1, street2)

    global ZIP
    pnts = get_lines_between_two_points(currentPoint, lonlat, street1, ZIP)

    if pnts == None:
        if debug:
            print "Cannot move any more"
        return None


    if len(pnts) > 1:
        pnts = pnts[1:]    

    f = distFrom(float(commuter[len(commuter)-1][0]), float(commuter[len(commuter)-1][1]), float(pnts[0][0]), float(pnts[0][1]))
    l = distFrom(float(commuter[len(commuter)-1][0]), float(commuter[len(commuter)-1][1]), float(pnts[len(pnts)-1][0]), float(pnts[len(pnts)-1][1]))
    
    if f > l:
        pnts.reverse()




    lastPoint = [float(commuter[len(commuter)-1][0]), float(commuter[len(commuter)-1][1])]
    delIndex = 0
    for i in pnts:
        if lastPoint[0] == float(i[0]) and lastPoint[1] == float(i[1]):
            delIndex += 1

    if debug:
        print "delIndex " + str(delIndex)
    
    if delIndex > 0:
        pnts = pnts[delIndex:]

    newList = []
    for i in range(len(pnts)-1):
        if pnts[i] != pnts[i+1]:
            newList.append(pnts[i])
    pnts = newList



    if debug:
        print "commuter " 
        print commuter
        print "attatch "
        print pnts

    for i in range (len(pnts)):
        commuter.append((float(pnts[i][0]), float(pnts[i][1])))
    


    if commuter[len(commuter)-1][0] != lonlat[0] and commuter[len(commuter)-1][1] != lonlat[1]:
        commuter.append( (float(lonlat[0]), float(lonlat[1])) )
        if debug:
            print "inserted"

    # commuter.append(lonlat)??????????????????????????

    commuters.get(str(commuterName))[1] = lonlat
    # commuters.get(str(commuterName))[2] = str(street2)
    commuters.get(str(commuterName))[3] = calculate_initial_compass_bearing( (commuter[len(commuter)-2][0], float(commuter[len(commuter)-2][1])) , (commuter[len(commuter)-1][0], float(commuter[len(commuter)-1][1])))
    
    if debug:
        print "last bearing : " + str(commuters.get(str(commuterName))[3])
        print commuters.get(str(commuterName))[0]
    
    return lonlat




#returns a road segment between start and end, area_code is used to filter out roads
def get_sub_road(road, area_code, start, end):
    
    query = "select ST_asText(tiger_data.get_sub_road('" + road + "' , '" + area_code + "' , " + start + " , " + end + "))"
    # print query
    
    #db = zxJDBC.connect(CONNECT_STRING, "postgres", "", "org.postgresql.Driver")
    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount

    if rowcount == 1:
        row = c.fetchone()
        c.close()
        db.close()

        if row[0] == None:
            return None
        # print "row"
        # print str(row)
        return extract_line_string(str(row))
    
def get_sub_road_distance(road, area_code, brng, point_EWKT, distance):
    
    dist_degrees = change_in_latitude(distance)
    #point_EWKT = get_EWKT(lonlatList)
    query = "select ST_asText( tiger_data.point_at_distance ('" + road + "' , '" + area_code + "', " + str(brng) + "," + point_EWKT + "," + str(dist_degrees) + "))"

    #db = zxJDBC.connect(CONNECT_STRING, "postgres", "", "org.postgresql.Driver")
    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount

    if rowcount == 1:
        row = c.fetchone()
        c.close()
        db.close()
        return extract_line_string(str(row))

def get_intersections2(street, start_geo, end_geo):

        points_bearing = calculate_initial_compass_bearing(start_geo, end_geo)

        if points_bearing >= 350:
                points_bearing = 0

        #print bearing , points_bearing

        if (points_bearing >= 260 and points_bearing <= 280) or bearing == 270:
                query = "select ST_asText(intersection.inters) from ( select distinct (ST_Intersection(a.the_geom, b.the_geom)) inters From tiger_data.in_roads a, (select fullname , the_geom, linearid from tiger_data.in_roads where fullname = '" + street + "' ) as b where st_intersects(a.the_geom, b.the_geom) and geometrytype(ST_Intersection(a.the_geom, b.the_geom)) = 'POINT'::text order by ST_Intersection(a.the_geom, b.the_geom) desc) intersection"
        else:
                query = "select ST_asText(intersection.inters) from ( select distinct (ST_Intersection(a.the_geom, b.the_geom)) inters From tiger_data.in_roads a, (select fullname , the_geom, linearid from tiger_data.in_roads where fullname = '" + street + "' ) as b where st_intersects(a.the_geom, b.the_geom) and geometrytype(ST_Intersection(a.the_geom, b.the_geom)) = 'POINT'::text order by ST_Intersection(a.the_geom, b.the_geom)) intersection"
        
        #db = zxJDBC.connect(CONNECT_STRING, "postgres", "", "org.postgresql.Driver")
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")

        c = db.cursor()
        c.execute(query)
        rowcount = c.rowcount

        lonlatList = []

        if rowcount > 1:
                
                for i in range (rowcount):
                        row = c.fetchone()      
                        lonlatList.append(extract_point(str(row)))
        c.close()
        db.close()

        output = test_intersection_points((start_geo[0], start_geo[1]), (end_geo[0], end_geo[1]), lonlatList)


        
        if (points_bearing >= 0 and points_bearing <= 10) :
                output = sorted(output, key = lambda x : (x[1], -x[0]))
                #print output
        elif points_bearing >= 170 and points_bearing <= 190:
                output = sorted(output, key = lambda x : (x[1], -x[0]), reverse = True)
                #print output

        #print output
        #print
        return output


     
def geocode_intersection(street1, street2):
        query =  "SELECT ST_AsText(geomout) FROM geocode_intersection('" + street1 + "','" + street2 + "','" + STATE + "','" + CITY + "','" + ZIP+"',1)"

        #print query
        
        #db = zxJDBC.connect(CONNECT_STRING, "postgres", "", "org.postgresql.Driver")
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        
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
                raise Exception("Geocode intersection " + street1 + ", " + street2 + " returned NULL")

def geocode_intersection2(street1, street2):
        query =  "SELECT ST_AsText(geomout) FROM geocode_intersection('" + street1 + "','" + street2 + "','" + STATE + "','" + CITY + "','" + ZIP+"',1)"

        # print query

        
        #db = zxJDBC.connect(CONNECT_STRING, "postgres", "", "org.postgresql.Driver")
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        
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
                #################### this is only difference
                return ""

def draw_line(geo1, geo2):
    instruction = "POLYLINE,"
    instruction += str(geo1[0]) + ";" + str(geo1[1]) + ","
    instruction += str(geo2[0]) + ";" + str(geo2[1]) + "\n"

    ExampleServiceImpl.setResultString(instruction)


#display the the list of coordinates on map
def show_on_map(commuterName):
        #db = zxJDBC.connect(CONNECT_STRING, "postgres", "", "org.postgresql.Driver")
        geoList = commuters.get(str(commuterName))[0]

        # db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        
        instruction = "POLYLINE,"
        for i in range(len(geoList)):
                instruction += str(geoList[i][0]) + ';' + str(geoList[i][1])+ ','
                
        instruction = instruction [: len(instruction)-1]
        instruction += "\n"
        ExampleServiceImpl.setResultString(instruction) 
    #     f = open("visualize.txt", 'a')
       # f.write(instruction)
       # f.close()
        
                                
def get(name, description, geomType):
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        c = db.cursor()
        if geomType == "POLYGON":
                if description == "STATE":
                        #query = "SELECT ST_AsText(ST_Simplify(the_geom, 0.05)) FROM tiger_data.state_all where name = '" + name +"'"
                        query = "SELECT ST_AsText(ST_SimplifyPreserveTopology(st_geometryn(the_geom,1), 0.05)) FROM tiger_data.state_all where name = '" + name +"'"
                        c.execute(query)
                        rowcount = c.rowcount

                        if rowcount == 1:
                                row = c.fetchone()
                                c.close()
                                db.close()
                                #print row[0]
                                return extract_polygon_v2(str(row))
                
                elif description == "CITY":
                    query = "select ST_AsText(ST_SimplifyPreserveTopology(st_geometryn(the_geom,1), 0.001)) from tiger_data.in_place where name = '" + name  + "%'"
                    c.execute(query)
                    rowcount = c.rowcount

                    if rowcount == 1:
                        row = c.fetchone()
                        c.close()
                        db.close()
                        #print row[0]
                        return extract_polygon_v2(str(row))
                        
                elif description == "COUNTY":
                        query = "SELECT ST_AsText(ST_SimplifyPreserveTopology(st_geometryn(the_geom,1), 0.05)) FROM tiger_data.county_all where name like  '" + name +"'"
                        print query
                        c.execute(query)
                        rowcount = c.rowcount

                        if rowcount > 0:
                                row = c.fetchone()
                                c.close()
                                db.close()
                                #print row[0]
                                return extract_polygon_v2(str(row))

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
                        #query = "Select ST_AsText(the_geom) from tiger_data.in_linearwater where fullname = '" + name + "'"
                        query = "Select ST_AsText(the_geom) from tiger_data.in_linearwater where lower(fullname) like '" + name + "'"
                        #print query
                        c.execute(query)
                        rowcount = c.rowcount
                        if rowcount >= 1:
                                lonlatList = []
                                for i in range (rowcount):
                                        row = c.fetchone()      
                                        lonlatList.append(extract_polyline(str(row)))
                                c.close()
                                db.close()
                                return lonlatList
                        
                                #row = c.fetchone()
                                #c.close()
                                #db.close()
                                #return extract_polyline(str(row))

                elif description == "PRIMARY-ROAD":
                        #query = "Select ST_AsText(the_geom) from tiger_data.in_linearwater where fullname = '" + name + "'"
                        query = "select ST_AsText(the_geom) from tiger_data.in_primaryroads where fullname = '" + name + "'"
                        #print query
                        c.execute(query)
                        rowcount = c.rowcount
                        if rowcount >1:
                                lonlatList = []
                                for i in range (rowcount):
                                        row = c.fetchone()      
                                        lonlatList.append(extract_polyline(str(row)))
                                c.close()
                                db.close()
                                return lonlatList

                elif description == "ROAD":
                        #query = "Select ST_AsText(the_geom) from tiger_data.in_linearwater where fullname = '" + name + "'"
                        query = "select ST_AsText(the_geom) from tiger_data.in_roads where fullname = '" + name + "'"
                        #print query
                        c.execute(query)
                        rowcount = c.rowcount
                        if rowcount >1:
                                lonlatList = []
                                for i in range (rowcount):
                                        row = c.fetchone()      
                                        lonlatList.append(extract_polyline(str(row)))
                                c.close()
                                db.close()
                                return lonlatList
      
        return "NULL"

def get_all(description, geomType):
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        c = db.cursor()
        if geomType == "POLYGON":
                if description == "STATE":
                        query = "SELECT ST_AsText(ST_SimplifyPreserveTopology(st_geometryn(the_geom,1), 0.05)) FROM tiger_data.state_all"
                        c.execute(query)
                        rowcount = c.rowcount

                        if rowcount > 1:
                                lonlatList = []
                                for i in range (rowcount):
                                        row = c.fetchone()      
                                        lonlatList.append(extract_polygon_v2(str(row)))
                                c.close()
                                db.close()
                                return lonlatList
                elif description == "COUNTY":
                        query = "SELECT ST_AsText(ST_SimplifyPreserveTopology(st_geometryn(the_geom,1), 0.05)) FROM tiger_data.county_all"
                        c.execute(query)
                        rowcount = c.rowcount

                        if rowcount > 1:
                                lonlatList = []
                                for i in range (rowcount):
                                        row = c.fetchone()      
                                        lonlatList.append(extract_polygon_v2(str(row)))
                                c.close()
                                db.close()
                                return lonlatList

        elif geomType == "POINT":
            if description == "STATE":
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
            elif description == "COUNTY":
                        query = "SELECT intptlon, intptlat from tiger_data.county_all"
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
            else:
                mtfcc = get_mtfcc(description)
                if mtfcc !="NULL":
                    query = "select  ST_X(the_geom), ST_Y(the_geom), fullname  from tiger_data.in_pointlm where mtfcc = '"+mtfcc+"'"
                    c.execute(query)
                    rowcount = c.rowcount
        
                    if rowcount > 1:
                        lonlatList = []
                        for i in range (rowcount):
                            row = c.fetchone()
                            lonlat = []
                            lonlat.append(row[0])
                            lonlat.append(row[1])
                            lonlat.append(str(row[2]))
                            lonlatList.append(lonlat)
                        c.close()
                        db.close()
                        return lonlatList
                                
        return "NULL"

#Get_Max_Postion returns position of maximum element of an array
def Get_Max_Postion(DistanceArray):
    if(len(DistanceArray) > 0):
        maxPostion = 0
        maxValue = DistanceArray[0]
        for i in range(len(DistanceArray)):
            if(DistanceArray[i]>maxValue):
                maxValue=DistanceArray[i]
                maxPostion=i
        return maxPostion
    else:
        return None

def get_coordinates(geoList):
    corr = ""
    for i in range(1,len(geoList)):
      corr += geoList[i][0] 
      corr +=" "
      corr +=geoList[i][1]
      if(i !=len(geoList)-1):
        corr+=" , "
   # print corr
    return corr
def getPolygonRange (geoList):
        Range =  []
        if(geoList[0] == "POLYGON"):
            maxLon = float(geoList[1][0]) 
            minLon = float(geoList[1][0])
            maxLat = float(geoList[1][1])
            minLat = float(geoList[1][1])
            #print maxLon , maxLat 
            for i in range(2,len(geoList)):
                #print geoList[i][0] , geoList[i][1]
                
                if(float(geoList[i][0]) > float(maxLon)):
                    maxLon = geoList[i][0]
                   # print "new maxlon" +  maxLon
                if(float(geoList[i][0])<float(minLon)):
                    minLon = geoList[i][0]
                  #  print "new minlon" +  minLon
                if(float(geoList[i][1])>float(maxLat)):
                    maxLat = geoList[i][1]
                    #print "new maxLat" +  maxLat
                if(float(geoList[i][1])<float(minLat)):
                    minLat = geoList[i][1]
                   # print "new minLat" +  minLat
            Range.append(maxLon)
            Range.append(minLon)
            Range.append(maxLat)
            Range.append(minLat)
        return Range
        
def get_kNN_optimized(description,Currentpoint,NumberOfItems,dist,Range):
    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    #print description , NumberOfItems  
    #print Currentpoint
    if Range == None:
        my_lat = Currentpoint[1]
        my_lon = Currentpoint[0]
        lon1 = my_lon - dist/abs(math.cos(math.radians(my_lat))*69)
        lon2 = my_lon + dist/abs(math.cos(math.radians(my_lat))*69)
        lat1 = my_lat-(dist/69.0) 
        lat2 = my_lat+(dist/69.0)
    else:
        #print "in"
        lon2 = float(Range[0])
        lon1 = float(Range[1])
        lat2 = float(Range[2])
        lat1 = float(Range[3])
        my_lat = (float(lat1)+float(lat2))/2
        my_lon = (float(lon1)+float(lon2))/2
        #print lon1 ,lon2 , lat1 , lat2,  my_lat , my_lon
    query = None
    

    if description == "STATE":
        if NumberOfItems == None:
            query = "SELECT intptlon, intptlat, name ,3958.75* 2 * ASIN(SQRT(POWER(SIN(("+str(my_lat)+"- abs(intptlat::numeric)) * pi()/180 / 2),2) + COS("+str(my_lat)+" * pi()/180 ) * COS(abs(intptlat::numeric) *pi()/180) * POWER(SIN(("+str(my_lon)+" -  intptlon::numeric) *pi()/180 / 2), 2) )) as distance from tiger_data.state_all  WHERE intptlat::numeric between "+ str(lat1) +" and "+str(lat2) + "AND intptlon::numeric between "+  str(lon1)+" and "+str(lon2)+ " ORDER BY distance "
        else:
            query =  "SELECT intptlon, intptlat, name ,3958.75* 2 * ASIN(SQRT(POWER(SIN(("+str(my_lat)+"- abs(intptlat::numeric)) * pi()/180 / 2),2) + COS("+str(my_lat)+" * pi()/180 ) * COS(abs(intptlat::numeric) *pi()/180) * POWER(SIN(("+str(my_lon)+" -  intptlon::numeric) *pi()/180 / 2), 2) )) as distance from tiger_data.state_all  WHERE intptlat::numeric between "+ str(lat1) +" and "+str(lat2) + "AND intptlon::numeric between "+  str(lon1)+" and "+str(lon2)+ " ORDER BY distance limit "+str( NumberOfItems)
    elif description == "COUNTY":
        if NumberOfItems == None:
            query = "SELECT intptlon, intptlat, name ,3958.75* 2 * ASIN(SQRT(POWER(SIN(("+str(my_lat)+"- abs(intptlat::numeric)) * pi()/180 / 2),2) + COS("+str(my_lat)+" * pi()/180 ) * COS(abs(intptlat::numeric) *pi()/180) * POWER(SIN(("+str(my_lon)+" -  intptlon::numeric) *pi()/180 / 2), 2) )) as distance from tiger_data.county_all  WHERE intptlat::numeric between "+ str(lat1) +" and "+str(lat2) + "AND intptlon::numeric between "+  str(lon1)+" and "+str(lon2)+ " ORDER BY distance "
        else:
            query =  "SELECT intptlon, intptlat, name ,3958.75* 2 * ASIN(SQRT(POWER(SIN(("+str(my_lat)+"- abs(intptlat::numeric)) * pi()/180 / 2),2) + COS("+str(my_lat)+" * pi()/180 ) * COS(abs(intptlat::numeric) *pi()/180) * POWER(SIN(("+str(my_lon)+" -  intptlon::numeric) *pi()/180 / 2), 2) )) as distance from tiger_data.county_all  WHERE intptlat::numeric between "+ str(lat1) +" and "+str(lat2) + "AND intptlon::numeric between "+  str(lon1)+" and "+str(lon2)+ " ORDER BY distance limit "+str( NumberOfItems)  
    else:
       mtfcc = get_mtfcc(description)
       if mtfcc != "NULL":
        if NumberOfItems == None:
            #print "in 2"
            query = "SELECT ST_X(the_geom),ST_Y(the_geom), fullname ,3958.75    * 2 * ASIN(SQRT(POWER(SIN(("+str(my_lat)+"- abs(ST_Y(the_geom))) * pi()/180 / 2),2) + COS("+str(my_lat)+" * pi()/180 ) * COS(abs(ST_Y(the_geom)) *pi()/180) * POWER(SIN(("+str(my_lon)+" -  ST_X(the_geom)) *pi()/180 / 2), 2) )) as distance FROM tiger_data.in_pointlm where mtfcc = '"+mtfcc+"'  AND ST_Y(the_geom) between "+str( lat1 )+" and "+ str( lat2)+ " AND ST_X(the_geom) between "+ str( lon1)+" and "+str( lon2) +" ORDER BY distance"
        else:
            query = "SELECT ST_X(the_geom),ST_Y(the_geom), fullname ,3958.75    * 2 * ASIN(SQRT(POWER(SIN(("+str(my_lat)+"- abs(ST_Y(the_geom))) * pi()/180 / 2),2) + COS("+str(my_lat)+" * pi()/180 ) * COS(abs(ST_Y(the_geom)) *pi()/180) * POWER(SIN(("+str(my_lon)+" -  ST_X(the_geom)) *pi()/180 / 2), 2) )) as distance FROM tiger_data.in_pointlm where mtfcc = '"+mtfcc+"'  AND ST_Y(the_geom) between "+str( lat1 )+" and "+ str( lat2)+ " AND ST_X(the_geom) between "+ str( lon1)+" and "+str( lon2)+" ORDER BY distance limit "+str( NumberOfItems)
    if(query is not None):
        #print "in3"
        c.execute(query)
        rowcount = c.rowcount
        #print query
        maxDistance = 0
        if rowcount > 0 :
            lonlatList = []
            for i in range (rowcount):
                row = c.fetchone()
                lonlat = []
                lonlat.append(row[0])
                lonlat.append(row[1])
                lonlat.append(row[2])
                lonlat.append(row[3])
                lonlatList.append(lonlat)
            c.close()
            db.close()
            return lonlatList
    c.close()
    db.close()   
    return "NULL"
#

def get_area(dist,description=None):
    if (description==None):
        if(type(dist) == int or type(dist) == float):
          return math.pi*dist*dist
        elif(dist[0]=="POLYGON"):
          corr =get_coordinates(dist)
          db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
          c = db.cursor()
          query = "SELECT ST_Area(ST_Transform(the_geom,26986)) FROM (SELECT ST_GeomFromText('POLYGON (("+corr+"))',4326)) As foo(the_geom)"
          c.execute(query)
          rowcount = c.rowcount
          if rowcount == 1:
            row = c.fetchone()
          c.close()
          db.close()
          return row[0]
  
    else:
        name=dist
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        c = db.cursor()
        if description == "STATE":
           query = "SELECT ST_Area(ST_Transform(the_geom,26986)) FROM tiger_data.state_all where name = '" + name +"'"
        elif description == "CITY":
           query = "select ST_Area(ST_Transform(the_geom,26986)) from tiger_data.in_place where name like '" + name+ "%'"
        c.execute(query)
        rowcount = c.rowcount
        if rowcount == 1:
          row = c.fetchone()
        c.close()
        db.close()
        return row[0]


def get_dim(testlist, dim=0):
   if isinstance(testlist, list):
      if testlist == []:
          return dim
      dim = dim + 1
      dim = get_dim(testlist[0], dim)
      return dim
   else:
      if dim == 0:
          return -1
      else:
          return dim
def get_count(Array):
    if get_dim(Array) == 2:
        return len(Array)
    else:
        count_array = []
        for j in range(len(Array)):
            if(Array[j] is not "NULL") :
                count =  len(Array[j])
            else:
                count = 0
            count_array.append(count)
        return count_array

def get_kNN_unoptimized(description,Currentpoint,NumberOfItems):
    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    query=None
    if description == "STATE":
            query = "SELECT intptlon, intptlat, name ,3958.75* 2 * ASIN(SQRT(POWER(SIN(("+str(Currentpoint[1])+"- abs(intptlat::numeric)) * pi()/180 / 2),2) + COS("+str(Currentpoint[1])+" * pi()/180 ) * COS(abs(intptlat::numeric) *pi()/180) * POWER(SIN(("+str(Currentpoint[0])+" -  intptlon::numeric) *pi()/180 / 2), 2) )) as distance from tiger_data.state_all ORDER BY distance limit "+str( NumberOfItems)
    elif description == "COUNTY":
            query = "SELECT intptlon, intptlat, name ,3958.75* 2 * ASIN(SQRT(POWER(SIN(("+str(Currentpoint[1])+"- abs(intptlat::numeric)) * pi()/180 / 2),2) + COS("+str(Currentpoint[1])+" * pi()/180 ) * COS(abs(intptlat::numeric) *pi()/180) * POWER(SIN(("+str(Currentpoint[0])+" -  intptlon::numeric) *pi()/180 / 2), 2) )) as distance from tiger_data.county_all ORDER BY distance limit "+str( NumberOfItems)
    else:
       mtfcc = get_mtfcc(description)
       if mtfcc != "NULL":
            query = "SELECT ST_X(the_geom),ST_Y(the_geom), fullname ,3958.75* 2 * ASIN(SQRT(POWER(SIN(("+str(Currentpoint[1])+"- abs(ST_Y(the_geom))) * pi()/180 / 2),2) + COS("+str(Currentpoint[1])+" * pi()/180 ) * COS(abs(ST_Y(the_geom)) *pi()/180) * POWER(SIN(("+str(Currentpoint[0])+" -  ST_X(the_geom)) *pi()/180 / 2), 2) )) as distance FROM tiger_data.in_pointlm where mtfcc = '"+mtfcc+"' ORDER BY distance limit "+str( NumberOfItems)
    if(query is not None):
        c.execute(query)
        rowcount = c.rowcount
        maxDistance = 0
        if rowcount > 0 :
            lonlatList = []
            for i in range (rowcount):
                row = c.fetchone()
                lonlat = []
                lonlat.append(row[0])
                lonlat.append(row[1])
                lonlat.append(row[2])
                lonlat.append(row[3])
                lonlatList.append(lonlat)
            c.close()
            db.close()
            return lonlatList
    c.close()
    db.close()   
    return "NULL"
#get_closest_point Returning N nearest points
def get_kNN(description,Currentpoint,NumberOfItems): 
        lonlatList = None
        
        if description == "STATE":
            lonlatList = get_kNN_optimized(description,Currentpoint,NumberOfItems,100*NumberOfItems,None)
        else:
            lonlatList= get_kNN_optimized(description,Currentpoint,NumberOfItems,10*NumberOfItems,None)
            if(len(lonlatList)<NumberOfItems):
                lonlatList = get_kNN_unoptimized(description,Currentpoint,NumberOfItems)
        if(lonlatList is not None):
            return lonlatList
               
        return "NULL"

def get_all_in_range(description,Currentpoint,dist=None):
    lonlatList = None
    if(dist == None and Currentpoint[0] == "POLYGON" ):
        Range = getPolygonRange(Currentpoint)
        #print Range
        lonlatList = get_kNN_optimized(description,None,None,None,Range)
    elif(dist == None and Currentpoint[0][0] == "POLYGON" ):
        count = 0
        lonlatList = []
        for i in range(len(Currentpoint)):
            lonlat = []
            Range = getPolygonRange(Currentpoint[i])
            lonlat = get_kNN_optimized(description,None,None,None,Range)
            #print lonlat
            lonlatList.append(lonlat)
        return lonlatList 
    else:
        lonlatList = get_kNN_optimized(description,Currentpoint,None,dist,None)
    if(lonlatList is not None):
        return lonlatList
    return "NULL"
def get_mtfcc(description):
    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    return_value  = None
    query="select mtfcc from tiger_data.mtfcc_Lookup where class_feature = lower('"+description+"')"
    c.execute(query)
    rowcount = c.rowcount
    if rowcount == 1 :
       row = c.fetchone()
       return_value = row[0]
    else :
       return_value= "NULL"
    c.close()
    db.close()
    
    #print return_value
    return return_value
def TEST():
    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    query=None
    #query = "SELECT table_name ,column_name FROM information_schema.columns WHERE table_name in (SELECT table_name FROM information_schema.tables )"
    #query = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'tiger_data' "
    #tiger_data.in_pointlm
    #query ="select  *  from tiger_data.mtfcc_Lookup where mtfcc like 'H%'"
    #query ="select  DISTINCT statefp , mtfcc from tiger_data.county_all"
    # ="select  fullname from tiger_data.in_roads where fullname like 'R%'"
    #query = "SELECT count(*)  from tiger_data.in_pointlm"
    query = " SELECT *  FROM tiger_data.county_all where name = 'DeKalb'"
    #query = "SELECT intptlon, intptlat from tiger_data.county_all"
    #state_all
    #query ="SELECT rolname FROM pg_roles"
    #query ="select column_name, data_type, character_maximum_length from INFORMATION_SCHEMA.COLUMNS where table_schema = 'tiger_data' and table_name = 'county_all'" 
    #query ="CREATE TABLE tiger_data.mtfcc_Lookup (    class_feature varchar(100),    mtfcc      varchar(5)PRIMARY KEY    )"
    #query = "INSERT INTO tiger_data.mtfcc_Lookup VALUES(lower('Mountain Peak or Summit'),'C3022')"
    if(query is not None):
        c.execute(query)
        rowcount = c.rowcount
        print rowcount
        if rowcount > 0 :
            for i in range (rowcount):
               row = c.fetchone()
               print row

    c.close()
    db.close()   
    return "NULL"    
        
def TEST2():
    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    query=None
    query = "SELECT  ST_CollectionExtract(the_geom,2), name from tiger_data.in_place limit 1"
    if(query is not None):
        c.execute(query)
        rowcount = c.rowcount
        print rowcount
        if rowcount > 0 :
            for i in range (rowcount):
                row = c.fetchone()
                print row

    c.close()
    db.close()   
    return "NULL"    
        
        
      

#get_closest_point Returning point of ck
def get_closest_point(description,Currentpoint,NumberOfItems):
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        c = db.cursor()
        if description == "STATE":
            query = "SELECT intptlon, intptlat from tiger_data.state_all"
            c.execute(query)
            rowcount = c.rowcount
            ClosestElement = None
            shortest_Distance = None
            if rowcount > 1:
                    
                    for i in range (rowcount):
                        row = c.fetchone()
                        lonlat = []
                        lonlat.append(row[0])
                        lonlat.append(row[1])
                        lonlat.append(str(row[2]))
                        distance = calculate_distance(Currentpoint, lonlat)
                        if shortest_Distance is None:
                            shortest_Distance= distance
                            ClosestElement = lonlat
                        if distance < shortest_Distance:
                            shortest_Distance= distance
                            ClosestElement = lonlat
                    c.close()
                    db.close()
                    return ClosestElement
        elif description == "COUNTY":
            query = "SELECT intptlon, intptlat from tiger_data.county_all"
            c.execute(query)
            rowcount = c.rowcount
            ClosestElement = None
            shortest_Distance = None
            if rowcount > 1:
                    
                    for i in range (rowcount):
                        row = c.fetchone()
                        lonlat = []
                        lonlat.append(row[0])
                        lonlat.append(row[1])
                        lonlat.append(str(row[2]))
                        distance = calculate_distance(Currentpoint, lonlat)
                        if shortest_Distance is None:
                            shortest_Distance= distance
                            ClosestElement = lonlat
                        if distance < shortest_Distance:
                            shortest_Distance= distance
                            ClosestElement = lonlat
                    c.close()
                    db.close()
                    return ClosestElement
        else :
            mtfcc = get_mtfcc(description)
            if mtfcc !="NULL":
                query = "select  ST_X(the_geom), ST_Y(the_geom), fullname  from tiger_data.in_pointlm where mtfcc = '"+mtfcc+"'"
                c.execute(query)
                rowcount = c.rowcount
                ClosestElement = None
                shortest_Distance  = None
                if rowcount > 1:
                    for i in range (rowcount):
                        row = c.fetchone()
                        lonlat = []
                        lonlat.append(row[0])
                        lonlat.append(row[1])
                        lonlat.append(str(row[2]))
                        distance = calculate_distance(Currentpoint, lonlat)
                        if shortest_Distance is None:
                                shortest_Distance= distance
                                ClosestElement = lonlat
                        if distance < shortest_Distance:
                                shortest_Distance= distance
                                ClosestElement = lonlat
                        c.close()
                        db.close()
                        return ClosestElement
                                
        return "NULL"

        
def display_Count(ArrayofShape,ArrayofDensity):
    if(len(ArrayofShape)==len(ArrayofDensity)):
        for i in range (len(ArrayofShape)):
            ArrayofShape[i][0]="FullPOLYGON"
            if(ArrayofDensity[i]==0):
                ArrayofShape[i].insert(1,"#FFFFFF")
            else:
                ArrayofShape[i].insert(1,get_fill(ArrayofDensity[i]))    
            instruction = create_geostring(ArrayofShape[i],1)
          #  print ArrayofShape[i][1]
            ExampleServiceImpl.setResultString(instruction)
        
def get_fill(ArrayofDensity):
    lighterBlue = (0xFF - (10*ArrayofDensity))
   # print "#0000%x" % lighterBlue
    return "#0000%x" % lighterBlue
        
#adds shape on map
def display_shape(shape):
        instruction = create_geostring(shape)
        ExampleServiceImpl.setResultString(instruction)
        # f = open("visualize.txt", 'a')
        # f.write(instruction)
        # f.close()
        

#creates a string of geocoordinates: lon1;lat,lon;lat,...
def create_geostring(geoList,Fill=0):
        instruction=""
        if(Fill!=0):
            instruction = geoList[0] + "," + geoList[1] + ","
            for i in range(2,len(geoList)):
                instruction += str(geoList[i][0]) + ';' + str(geoList[i][1])+ ','
        else:
            instruction = geoList[0] + ","
            for i in range(1,len(geoList)):
                    instruction += str(geoList[i][0]) + ';' + str(geoList[i][1])+ ','
        instruction = instruction [: len(instruction)-1]
        instruction += "\n"
        #print instruction
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
        #print polygon
        return polygon

#create a polyline geometry
def create_polyline(coord_list):
       geomfactx = GeometryFactory()
       line = geomfactx.createLineString(coord_list)
       #print line
       return line

def create_shape(geoList):
       
        coord_list = create_coorList(geoList)
        if geoList[0] == "POLYGON":
                shape = create_polygon(coord_list)
                return shape
        elif geoList[0] == "POLYLINE":
                #print "polyline"
                shape = create_polyline(coord_list)
                return shape
                
        return "NULL"

# returns true if shape1 touches shape2
def touches(geoList1, geoList2):
        shape1 = create_shape(geoList1)
        shape2 = create_shape(geoList2)
#       print shape1.distance(shape2)

        if shape1.touches(shape2):
       # if shape1.distance(shape2) == 0.0:
                return True
        return False
 

def disjoint(geoList1, geoList2):
        shape1 = create_shape(geoList1)
        shape2 = create_shape(geoList2)
#       print shape1.distance(shape2)

        if shape1.disjoint(shape2):
       # if shape1.distance(shape2) == 0.0:
                return True
        return False
        
def crosses(geoList1, geoList2):
        shape1 = create_shape(geoList1)
        shape2 = create_shape(geoList2)
#       print shape1.distance(shape2)

        if shape1.crosses(shape2):
       # if shape1.distance(shape2) == 0.0:
                return True
        return False
        
def equals(geoList1, geoList2):
        shape1 = create_shape(geoList1)
        shape2 = create_shape(geoList2)
#       print shape1.distance(shape2)

        if shape1.equals(shape2):
       # if shape1.distance(shape2) == 0.0:
                return True
        return False
# returns true if shape1 intersects shape2
def intersects(geoList1, geoList2):
        shape1 = create_shape(geoList1)
        shape2 = create_shape(geoList2)
        
        if shape1.intersects(shape2):
              if shape1.contains(shape2) or shape2.contains(shape1): 
                return False
              else:
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
    
# returns true if shape1 within shape2
def within(geoList1, geoList2):
        shape1 = create_shape(geoList1)
        shape2 = create_shape(geoList2)

        if shape1.within(shape2):
                return True
        return False

    
# returns true if shape1 within shape2
def compute_shape_area(geoList1):
        shape1 = create_shape(geoList1)
        return shape1.getArea()
                
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

#calculate the distance commuted
def caculate_distance_commuted2(commuter):
        result = 0
        # commuter = commuters.get(str(commuterName))[0]
        for i in range(len(commuter)-1):
                result = result + calculate_distance(commuter[i], commuter[i+1])
        return round(result,2)
    
def caculate_distance_commuted(commuterName):
        result = 0
        commuter = commuters.get(str(commuterName))[0]
        for i in range(len(commuter)-1):
                result = result + calculate_distance(commuter[i], commuter[i+1])
        return round(result,2)

#calculate the distance commuted
def display_distance(commuterName):
        return caculate_distance_commuted(commuterName)


def get_road_names(point):
    if len(point) != 2:
        raise Exception("Point should be geo-location!")

    # print point

    names = findAllIntersectionRoads((point[0], point[1]), 0.009)

    return names


#move to the next intersection within specified distance
def move_to_next_intersection(commuterName):
    commuter = commuters.get(str(commuterName))[0]
    currentPoint = commuters.get(str(commuterName))[1]
    current_street = commuters.get(str(commuterName))[2]
    bearing = commuters.get(str(commuterName))[3]
    

    global ZIP
    next = get_next_intersection(currentPoint[0], currentPoint[1], current_street, ZIP, bearing)

    # print "----"
    # print str(currentPoint[0]) + " + " + str(currentPoint[1])
    # print show_commuter(commuterName)

    move_until(commuterName, next[0])


def distance_all_roads_in(geo1, geo2, geo3, geo4):
    line = ((geo1, geo2, geo3, geo4))

    lineString = "LINESTRING("

    for item in line:
        lineString += str(item[0])
        lineString += " "
        lineString += str(item[1])
        lineString += ","

    lineString += str(line[0][0]) + " " + str(line[0][1]) + ")"
    
    #Harsh: added some stuff to the query

    query = "select ST_AsText(ST_Intersection(T1.geom, ST_Polygon(ST_GeomFromText('" + lineString + "'),4269))) from (select the_geom geom from tiger_data.in_roads r where st_intersects(ST_Polygon(ST_GeomFromText('" + lineString + "'),4269),r.the_geom)) T1"
                    
    #print query

    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount
    
    roads = []

    for i in range(rowcount):
        st = c.fetchone()
        roads.append(str(st[0]))

    c.close()
    db.close()
    
    sum = 0.0
    for road in roads:
        # print road

        trace = []
        road = road[11:len(road)-1]
        geoList = road.split(",")

        for geo in geoList:
            geo = geo.split(" ")
            trace.append([float(geo[0]),float(geo[1])])


        sum += caculate_distance_commuted2(trace)

    # print sum

    return sum


def draw_all_roads_in(geo1, geo2, geo3, geo4):
    line = ((geo1, geo2, geo3, geo4))

    lineString = "LINESTRING("

    for item in line:
        lineString += str(item[0])
        lineString += " "
        lineString += str(item[1])
        lineString += ","

    lineString += str(line[0][0]) + " " + str(line[0][1]) + ")"

    query = "select ST_AsText(ST_Intersection(T1.geom, ST_Polygon(ST_GeomFromText('" + lineString + "'),4269)))  from (select the_geom geom from tiger_data.in_roads r where st_intersects(ST_Polygon(ST_GeomFromText('" + lineString + "'),4269),r.the_geom)) T1"

    #print query

    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount
    
    roads = []

    for i in range(rowcount):
        st = c.fetchone()
        roads.append(str(st[0]))

    c.close()
    db.close()

    # print roads

    # instruction = "DRAWROADS,"+ str(geolocation[0]) + "," + str(geolocation[1]) + "\n"
    
    
    
    for road in roads:
        # print road
        instruction = "POLYLINE2,"

        road = road[11:len(road)-1]
        # print road
        geoList = road.split(",")

        for geo in geoList:
            geo = geo.split(" ")
            instruction += str(geo[0]) + ';' + str(geo[1])+ ','

        instruction = instruction[:len(instruction) - 1]
        instruction += "\n"

        ExampleServiceImpl.setResultString(instruction)

        # print instruction
        # instruction += "/"    
            
    # instruction = instruction[:len(instruction)-1]
    # instruction += "\n"

    # print instruction
    # ExampleServiceImpl.setResultString(instruction)
    

def get_road_names_in(geo1, geo2, geo3, geo4):
    return findAllIntersectionRoads2((geo1, geo2, geo3, geo4))


def get_angle_between(a1, a2):
    a = 180 - abs(abs(a1 - a2) - 180)
    return a

def findAllIntersectionRoads2(testCommuter):
    # print testCommuter
    # print "@"
    # print testCommuter
    # if len(testCommuter) < 4:
        # print "?"
        # return ""
    lineString = "LINESTRING("

    for item in testCommuter:
        lineString += str(item[0])
        lineString += " "
        lineString += str(item[1])
        lineString += ","

    lineString += str(testCommuter[0][0]) + " " + str(testCommuter[0][1]) + ")"

    query = "select distinct fullname from tiger_data.in_roads r where st_intersects(ST_Polygon(ST_GeomFromText('" +lineString+ "'),4269),r.the_geom)"

    # print query

    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    try:
        c.execute(query)
    except:
        print lineString
        c.close()
        db.close()
        return None
    rowcount = c.rowcount
    
    roads = []

    for i in range(rowcount):
        st = c.fetchone()
        roads.append(str(st[0]))
        


    c.close()
    db.close()

    # print roads

    return roads


def findAllIntersectionRoads(currentPoint, eps):
    currentSquare = [compute_next_point(currentPoint[0], currentPoint[1], 0, eps),    
                            compute_next_point(currentPoint[0], currentPoint[1], 90, eps),
                            compute_next_point(currentPoint[0], currentPoint[1], 180, eps),
                            compute_next_point(currentPoint[0], currentPoint[1], 270, eps)]

    lineString = "LINESTRING(" + str(currentSquare[0][0]) + " " + str(currentSquare[0][1]) + "," + \
                                str(currentSquare[1][0]) + " " + str(currentSquare[1][1]) + "," + \
                                str(currentSquare[2][0]) + " " + str(currentSquare[2][1]) + "," + \
                                str(currentSquare[3][0]) + " " + str(currentSquare[3][1]) + "," + \
                                str(currentSquare[0][0]) + " " + str(currentSquare[0][1]) + ")"
    # print lineString
                            
    query = "select distinct fullname from tiger_data.in_roads r where st_intersects(ST_Polygon(ST_GeomFromText('" +lineString+ "'),4269),r.the_geom)"
    # print query

    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount
    
    roads = []

    for i in range(rowcount):
        st = c.fetchone()
        roads.append(str(st[0]))
        


    c.close()
    db.close()

    return roads



def get_next_intersection(lon, lat, street, ZIP, bearing):
    fraction = get_current_fraction_on_the_street(lon, lat, street, ZIP)

    # print "fraction, lon, lat = ",
    #print fraction
    #print lon
    #print lat

    inter1 = get_all_intersection_name_and_point_on_the_road_with_fraction(0, fraction, street, ZIP)
    inter2 = get_all_intersection_name_and_point_on_the_road_with_fraction(fraction, 1, street, ZIP)

    # get smallest distance for each inters


    minDist1 = 10000
    st1 = []

    
    for item in inter1:
        
        point = item[1]
        point = point[6:len(point)-1].split(" ")

        f1 = float(point[0])
        f2 = float(point[1])

        dist = distFrom(float(lon), float(lat), f1, f2)
        
        if dist < minDist1:
            if float(lon) != f1 and float(lat) != f2:
                minDist1 = dist
                st1 = [(str(item[0])), point]
    

    minDist2 = 10000
    st2 = []
    for item in inter2:
        
        point = item[1]
        point = point[6:len(point)-1].split(" ")


        f1 = float(point[0])
        f2 = float(point[1])

        dist = distFrom(float(lon), float(lat), f1, f2)
        
        if dist < minDist2:
            if float(lon) != f1 and float(lat) != f2:
                minDist2 = dist
                st2 = [(str(item[0])), point]

        #print st1
        #print inter1
        #print "#$"
        #print st2
        #print inter2

    # br1 = calculate_initial_compass_bearing((float(st1[1][0]), float(st1[1][0])), (float(lat), float(lon)))
    # br2 = calculate_initial_compass_bearing((float(st2[1][0]), float(st2[1][0])), (float(lat), float(lon)))

    if len(st1) == 0:
        return st2
    else:
        br1 = calculate_initial_compass_bearing((float(lon), float(lat)), (float(st1[1][0]), float(st1[1][1])))
    
    if len(st2) == 0:
        return st1
    else:
        br2 = calculate_initial_compass_bearing((float(lon), float(lat)), (float(st2[1][0]), float(st2[1][1])))

    if get_angle_between(bearing, br1) < get_angle_between(bearing, br2):
        return st1
    else:
        return st2

def get_lines_between_two_points(currentPoint, nextPoint, street, ZIP):
    fr1 = get_current_fraction_on_the_street(float(currentPoint[0]), float(currentPoint[1]), street, ZIP)
    fr2 = get_current_fraction_on_the_street(float(nextPoint[0]), float(nextPoint[1]), street, ZIP)

    if fr1 > fr2:
        temp = fr2
        fr2 = fr1
        fr1 = temp

    query = "select ST_AsText(ST_Line_SubString(ST_LineMerge(a.the_geom), " + str(fr1) + ", " + str(fr2) + ")) line from (select the_geom from tiger_data.in_roads where fullname = '" + street + "') as a, (select the_geom from tiger_data.in_zcta5 where zcta5ce = '" + str(ZIP) + "') as b  where ST_Intersects(a.the_geom, ST_Simplify(b.the_geom,0.001)) order by ST_Length(ST_LineMerge(st_geometryn(a.the_geom,1))) desc LIMIT 1"

    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount
    
    ret = []

    for i in range(rowcount):
        st = c.fetchone()
        ret.append(str(st[0]))

    #try:
    if len(ret) == 0:
        return None

    line = str(ret[0])

    c.close()
    db.close()

    if line[0:5] == "POINT":
        return None

    # print "00000000"
    # print line
    # print extract_line_string2(line)

    return extract_line_string2(line)






def get_total_length_of_street(street, ZIP):
    query = "select (ST_Length(ST_Transform(ST_LineMerge(st_geometryn(a.the_geom,1)),2877))/5280) from (select the_geom from tiger_data.in_roads where fullname = '" + street + "') as a, (select the_geom from tiger_data.in_zcta5 where zcta5ce = '" + str(ZIP) + "') as b where ST_Intersects(a.the_geom, ST_Simplify(b.the_geom,0.001)) "

    # print query

    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount
    
    ret = []

    for i in range(rowcount):
        st = c.fetchone()
        ret.append(str(st[0]))

    #try:
    if len(ret) == 0:
        return None

    totalLength = float(ret[0])

    c.close()
    db.close()

    return totalLength


def get_current_fraction_on_the_street(lon, lat, street, ZIP):

    query = "select ST_Line_Locate_Point( (select ST_LineMerge(st_geometryn(a.the_geom,1)) from (select the_geom from tiger_data.in_roads where fullname = '" + street + "') as a, (select the_geom from tiger_data.in_zcta5 where zcta5ce = '" + str(ZIP) + "') as b where ST_Intersects(a.the_geom, ST_Simplify(b.the_geom,0.001)) order by ST_Length(ST_LineMerge(st_geometryn(a.the_geom,1))) desc LIMIT 1),  ST_GeomFromText('POINT (" + str(lon) + " " + str(lat) + ")',4269))"
    
    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount
    
    ret = []

    for i in range(rowcount):
        st = c.fetchone()
        ret.append(str(st[0]))

    if len(ret) == 0:
        return None
    
    fraction = float(ret[0])

    c.close()
    db.close()

    return fraction



def get_all_intersection_name_and_point_on_the_road(street, ZIP):
    query = "select T2.fullname fullname, ST_AsText(ST_Intersection(T1.geom, T2.geom)) point from ( select ST_LineMerge(a.the_geom) geom  from (select the_geom from tiger_data.in_roads where fullname = '" + street + "') as a, (select the_geom from tiger_data.in_zcta5 where zcta5ce = '" + str(ZIP) + "') as b  where ST_Intersects(a.the_geom, ST_Simplify(b.the_geom,0.001)) ) as T1, ( select ST_LineMerge(c.the_geom) geom, fullname from (select the_geom, fullname from tiger_data.in_roads) as c, (select the_geom from tiger_data.in_zcta5 where zcta5ce = '" + str(ZIP) + "') as d   where ST_Intersects(c.the_geom, ST_Simplify(d.the_geom,0.001))) as T2  where ST_Intersects(T1.geom, T2.geom) and ST_GeometryType(ST_Intersection(T1.geom, T2.geom)) = 'ST_Point'"

    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD,
                        "org.postgresql.Driver")

    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount
    
    ret = []

    for i in range(rowcount):
        st = c.fetchone()
        ret.append(( str(st[0]), str(st[1]) ) )

    return ret

def get_all_intersection_name_and_point_on_the_road_with_fraction(
        fractionFrom, fractionTo, street, ZIP):
    query = "select T2.fullname fullname, ST_AsText(ST_Intersection(T1.geom, T2.geom)) point   from ( select ST_Line_SubString(ST_LineMerge(a.the_geom), " + str(fractionFrom) + ", " + str(fractionTo) + ") geom    from (select the_geom from tiger_data.in_roads where fullname = '" + street + "') as a, (select the_geom from tiger_data.in_zcta5 where zcta5ce = '" + str(ZIP) + "') as b   where ST_Intersects(a.the_geom, ST_Simplify(b.the_geom,0.001)) ) as T1,  ( select ST_LineMerge(c.the_geom) geom, fullname from (select the_geom, fullname from tiger_data.in_roads) as c, (select the_geom from tiger_data.in_zcta5 where zcta5ce = '" + str(ZIP) + "') as d where ST_Intersects(c.the_geom, ST_Simplify(d.the_geom,0.001))) as T2 where ST_Intersects(T1.geom, T2.geom) and ST_GeometryType(ST_Intersection(T1.geom, T2.geom)) = 'ST_Point'"

    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")

    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount
    
    ret = []

    for i in range(rowcount):
        st = c.fetchone()
        ret.append(( str(st[0]), str(st[1]) ) )

    return ret


def move_along_street(lon, lat, street, ZIP, bearing, distance):
    

    totalLength = get_total_length_of_street(street, ZIP)

    if debug:
        print "total length : " + str(totalLength)

    
    startFraction = get_current_fraction_on_the_street(lon, lat, street, ZIP)
    
    if debug:
        print "startFraction " + str(startFraction)
        print "dist : " + str(distance)


    distanceFraction = distance / totalLength

    nextFraction1 = startFraction - distanceFraction
    nextFraction2 = startFraction + distanceFraction


    if debug:
        print "distanceFraction " + str(distanceFraction)
        print "1 : " + str(nextFraction1)
        print "2 : " + str(nextFraction2)
    
    nextPoint1 = ""
    nextPoint2 = ""
    if nextFraction1 < 0:
        nextFraction1 = 0;
    elif nextFraction1 > 1:
        nextFraction1 = 1

    if nextFraction2 < 0:
        nextFraction2 = 0;
    elif nextFraction2 > 1:
        nextFraction2 = 1

    if 0 <= nextFraction1 and nextFraction1 <= 1:
        query ="select ST_AsText(ST_Line_Interpolate_Point( (select ST_LineMerge(a.the_geom) from (select the_geom from tiger_data.in_roads where fullname = '" +  street + "') as a,  (select the_geom from tiger_data.in_zcta5 where zcta5ce = '" +  ZIP + "') as b   where ST_Intersects(a.the_geom, ST_Simplify(b.the_geom,0.001)) LIMIT 1), " + str(nextFraction1) + "))"
        # print query
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        c = db.cursor()
        c.execute(query)
        rowcount = c.rowcount
        
        ret = []

        for i in range(rowcount):
            st = c.fetchone()
            ret.append(str(st[0]))
        
        nextPoint1 = ret[0][6:len(ret[0])-1].split(" ")
        # if debug:
        #     print "Have ONE!!!"
        #     print nextPoint1

    if 0 <= nextFraction2 and nextFraction2 <= 1:
        query = "select ST_AsText(ST_Line_Interpolate_Point( (select ST_LineMerge(a.the_geom) from (select the_geom from tiger_data.in_roads where fullname = '" +  street + "') as a, (select the_geom from tiger_data.in_zcta5 where zcta5ce = '" +  ZIP + "') as b where ST_Intersects(a.the_geom, ST_Simplify(b.the_geom,0.001)) LIMIT 1), " + str(nextFraction2) + "))"

        # db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        # c = db.cursor()
        c.execute(query)
        rowcount = c.rowcount
        
        ret = []

        for i in range(rowcount):
            st = c.fetchone()
            ret.append(str(st[0]))
        
        nextPoint2 = ret[0][6:len(ret[0])-1].split(" ")
        
        # if debug:
        #     print "Have ONE!!!"
        #     print nextPoint2


    c.close()
    db.close()

    nextPoint = [0,(),[],[]]
    try:
        if nextPoint1 == "" and nextPoint2 == "":
            if debug:
                print "Have no"
            nextPoint[0] = 0
             

        if nextPoint1 != "" and nextPoint2 != "":
            bearing1 = calculate_initial_compass_bearing( (float(lon), float(lat)), (float(nextPoint1[0]), float(nextPoint1[1]))) 
            bearing2 = calculate_initial_compass_bearing( (float(lon), float(lat)), (float(nextPoint2[0]), float(nextPoint2[1])))

            if debug:
                print "Have two : "
                print bearing
                print bearing1
                print bearing2

            nextPoint[0] = 2


            minTo1 = get_angle_between(bearing, bearing1)
            minTo2 = get_angle_between(bearing, bearing2)
         
            if minTo1 < minTo2:
                # print "!!3"
                nextPoint[1] = (float(nextPoint1[0]), float(nextPoint1[1]))
                nextPoint[2].append((float(nextPoint1[0]), float(nextPoint1[1])))
                nextPoint[2].append((float(nextPoint2[0]), float(nextPoint2[1])))
                nextPoint[3].append(bearing1)
                nextPoint[3].append(bearing2)
                # print "3"
                
            else:
                # print "!!4"
                nextPoint[1] = (float(nextPoint2[0]), float(nextPoint2[1]))
                nextPoint[2].append((float(nextPoint2[0]), float(nextPoint2[1])))
                nextPoint[2].append((float(nextPoint1[0]), float(nextPoint1[1])))
                nextPoint[3].append(bearing2)
                nextPoint[3].append(bearing1)
                # print "4"

           
    except:
        print "abcde"
        print nextPoint1
        print nextPoint2
        # return None            

    if debug:
        print "Next is ",
        print nextPoint

    return nextPoint


def move_distance(commuterName, distance, direction=None):

    commuter = commuters.get(str(commuterName))[0]
    currentPoint = commuters.get(str(commuterName))[1]

    street = commuters.get(str(commuterName))[2]


    commuters.get(str(commuterName))[4] = []
    tmp = deepcopy(commuters.get(str(commuterName))[0])
    commuters.get(str(commuterName))[4].append(tmp)

    tmp = deepcopy(commuters.get(str(commuterName))[1])
    commuters.get(str(commuterName))[4].append(tmp)

    tmp = deepcopy(commuters.get(str(commuterName))[2])
    commuters.get(str(commuterName))[4].append(tmp)

    tmp = deepcopy(commuters.get(str(commuterName))[3])
    commuters.get(str(commuterName))[4].append(tmp)



    if direction == None:
        bearing = commuters.get(str(commuterName))[3]
    else:
        if str(direction).isdigit():
            bearing = float(direction)
        else:
            bearing = orient_to(commuterName, direction)

     #get the previous commuter geo-coordinate
    
    lonlat = move_along_street(currentPoint[0], currentPoint[1], street, ZIP, bearing, distance)

    if lonlat == None:
        return None

    if lonlat[0] == 0:
        if debug:
            print "no next point"
        return None

    if lonlat[0] == 5:
        if debug:
            print "no next point!!"
        return False


    pnts=[]
    
    if lonlat[3][0] != 0.0:
        lonlat = lonlat[1]
        pnts = get_lines_between_two_points(currentPoint, lonlat, street, ZIP)
    else:
        return None

    if pnts == None:
        if debug:
            print "Cannot move any more"
        return None


    if len(pnts) > 1:
        pnts = pnts[1:]    

    f = distFrom(float(commuter[len(commuter)-1][0]), float(commuter[len(commuter)-1][1]), float(pnts[0][0]), float(pnts[0][1]))
    l = distFrom(float(commuter[len(commuter)-1][0]), float(commuter[len(commuter)-1][1]), float(pnts[len(pnts)-1][0]), float(pnts[len(pnts)-1][1]))
    
    if f > l:
        pnts.reverse()




    lastPoint = [float(commuter[len(commuter)-1][0]), float(commuter[len(commuter)-1][1])]
    delIndex = 0
    for i in pnts:
        if lastPoint[0] == float(i[0]) and lastPoint[1] == float(i[1]):
            delIndex += 1

    if debug:
        print "delIndex " + str(delIndex)
    
    if delIndex > 0:
        pnts = pnts[delIndex:]

    newList = []
    for i in range(len(pnts)-1):
        if pnts[i] != pnts[i+1]:
            newList.append(pnts[i])
    pnts = newList



    if debug:
        print "commuter " 
        print commuter
        print "attatch "
        print pnts

    for i in range (len(pnts)):
        commuter.append((float(pnts[i][0]), float(pnts[i][1])))
    


    if commuter[len(commuter)-1][0] != lonlat[0] and commuter[len(commuter)-1][1] != lonlat[1]:
        commuter.append( (float(lonlat[0]), float(lonlat[1])) )
        if debug:
            print "inserted"

    # commuter.append(lonlat)??????????????????????????

    commuters.get(str(commuterName))[1] = lonlat
    # commuters.get(str(commuterName))[2] = str(street2)
    commuters.get(str(commuterName))[3] = calculate_initial_compass_bearing( (commuter[len(commuter)-2][0], float(commuter[len(commuter)-2][1])) , (commuter[len(commuter)-1][0], float(commuter[len(commuter)-1][1])))
    
    if debug:
        print "last bearing : " + str(commuters.get(str(commuterName))[3])
        print commuters.get(str(commuterName))[0]
    
    return lonlat




#verifies whether or not the point is on the street 
def verify_street_point(street, nextLonLat):

        query = "select ST_asText(ST_StartPoint(ST_GeometryN(ST_Multi(the_geom),1))) , ST_asText(ST_EndPoint(ST_GeometryN(ST_Multi(the_geom),1))) from tiger_data.in_roads where fullname = '" + street + "'"
           
        #db = zxJDBC.connect(CONNECT_STRING, "postgres", "", "org.postgresql.Driver")
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        c = db.cursor()
        c.execute(query)
        rowcount = c.rowcount

        lines_table = []
        
        if rowcount > 1:
                for i in range (rowcount):
                        line = []
                        row = c.fetchone()
                        start = extract_point_v2(row[0])
                        end = extract_point_v2(row[1])
                        line.append(start)
                        line.append(end)
                        lines_table.append(line)

        c.close()
        db.close()

        for i in range(len(lines_table)):
                pt1 = lines_table[i][0]
                pt2 = lines_table[i][1]

                if test_line_point(pt1, pt2, nextLonLat):
                   return True
        return False

#test whether the point on the line represented by start and end       
def test_line_point(start, end, pt):

        lon1, lat1 = polarToCartesian(start)
        lon2, lat2 = polarToCartesian(end)
        pt_lon, pt_lat = polarToCartesian(pt)

        dist = projectedDistance(start, end, pt)
        projected_point = projectPointToLine(start, end, pt)

        if acceptPoint(start, end, projected_point, dist):
                return True
        return False
        
      
#compute the next point based on the current lonlat, orientation (bearing) and distance
def compute_next_point(lon1, lat1, brng, dist):

        dist = dist/3959.0
        brng = math.radians(brng)
        lat1 = math.radians(lat1)
        lon1 = math.radians(lon1)

        lat2 = math.asin( math.sin(lat1)*math.cos(dist) + math.cos(lat1)*math.sin(dist)*math.cos(brng) )
        a = math.atan2(math.sin(brng)*math.sin(dist)*math.cos(lat1), math.cos(dist)-math.sin(lat1)*math.sin(lat2))
        lon2 = lon1 + a

        lon2 = (lon2+ 3*math.pi) % (2*math.pi) - math.pi

        lat2 = math.degrees(lat2)
        lon2 = math.degrees(lon2)

        return [lon2, lat2]
 
def get_current_point(commuterName):
    return commuters.get(str(commuterName))[1]       


def turn_to(commuterName, roadName, direction = None):

    commuters.get(str(commuterName))[4] = []
    tmp = deepcopy(commuters.get(str(commuterName))[0])
    commuters.get(str(commuterName))[4].append(tmp)

    tmp = deepcopy(commuters.get(str(commuterName))[1])
    commuters.get(str(commuterName))[4].append(tmp)

    tmp = deepcopy(commuters.get(str(commuterName))[2])
    commuters.get(str(commuterName))[4].append(tmp)

    tmp = deepcopy(commuters.get(str(commuterName))[3])
    commuters.get(str(commuterName))[4].append(tmp)

    global ZIP
    
    
    if direction == None:
        commuters.get(str(commuterName))[2] = str(roadName)

        nextPoint = move_along_street(commuters.get(str(commuterName))[1][0], commuters.get(str(commuterName))[1][1], str(roadName)\
                            ,ZIP, commuters.get(str(commuterName))[3], 0.01)
        # print "123"
        # print nextPoint
        # print "456"
        if nextPoint == None:
            return []
        elif nextPoint[0] == 1:
            direction = nextPoint[3][0]
            commuters.get(str(commuterName))[3] = direction
            return nextPoint[3]

        elif nextPoint[0] == 2:

            direction = nextPoint[3][0]
            commuters.get(str(commuterName))[3] = direction
            return nextPoint[3]


    else:
        commuters.get(str(commuterName))[2] = str(roadName)
        
        if str(direction).isdigit():
            direction = int(direction)
        else:
            direction = orient_to(commuterName, direction)

        commuters.get(str(commuterName))[3] = direction

        return [direction]
        # print roadName
        # print direction
        # print "3"
    

#turn right will increase the bearing by 90 degrees
def turn_right(street, commuter):
    global bearing
    
    previous_bearing = bearing
    bearing = (bearing + 90)%360
        
    #update commuter if needed
    #in the case of Move_distance, the specified user distance might be a little bit
    #after the turn, we modify the commuter points according to the street
    #specified in the turn

    update_commuter_turn(street, bearing, previous_bearing, commuter)

#when a commuter makes a turn after a move_distace, it is expected that the distance will put the 
#commuter after the turn which result in incorrect path. we remove the extra points    
def update_commuter_turn(street, bearing, previous_bearing, commuter):
    
    global last_move
    if last_move == True:
        last_move = False
        return
 
    last_point = commuter[len(commuter)-1]
    LON = str(last_point[0])
    LAT = str(last_point[1])
    
    query =  "select * from tiger_data.street_at_point(ST_SetSRID(ST_Point("+ LON + ", " + LAT + "),4269))"
    
    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount

    if rowcount == 1:
        
        row = c.fetchone()
        street2 =  str(row[0])
        
        if street == street2:
            return
        
        new_point = geocode_intersection(street, street2)
        #if street == "Oakhurst Dr":
            #print query
            #print new_point
            
        #if street == "Stadium Ave":
            #print query
            #print new_point
                
        temp = compute_next_point(new_point[0], new_point[1], bearing, 0.25)
                   
        opposite_bearing = (bearing + 180) % 360
        
        temp2 = compute_next_point(new_point[0], new_point[1], opposite_bearing , 0.25)
        
        initial_orientation_point = compute_next_point(commuter[len(commuter)-1][0], commuter[len(commuter)-1][1], previous_bearing, 0.25)
                
        initial_orientation = cross_product(polarToCartesian(temp), polarToCartesian(temp2), polarToCartesian(initial_orientation_point))
        
        #print temp, temp2
                
        for item in commuter[::-1]:
            if initial_orientation == cross_product(polarToCartesian(temp), polarToCartesian(temp2), polarToCartesian(item)) and calculate_distance(new_point, item) > 0.005:
                #print calculate_distance(new_point, item)
                commuter.pop()
            else:
                break
        commuter.append(new_point)
                                            
    c.close()
    db.close()

#turn left will decrease the bearing by 90 degrees
def turn_left(street, commuter):
        global bearing
        
        previous_bearing = bearing
        if bearing == 0:
                bearing = 270
        else:
                bearing = (bearing - 90)%360
                
        #print street, bearing
        update_commuter_turn(street, bearing, previous_bearing, commuter)
def isReal(txt):
    try:
        float(txt)
        return True
    except ValueError:
        return False

#initialize the bearing, needed for relative instructions
def orient_to(commuterName, direction):
        global commuters
        bearing = 0
        
        if isReal(direction):
            if str(commuterName) in commuters:
                commuters.get(str(commuterName))[3] = bearing            
            bearing = direction
            return bearing
        
        if direction.upper() == "NORTH":
                bearing = 0
        elif direction.upper() == "EAST":
                bearing = 90
        elif direction.upper() == "SOUTH":
                bearing = 180
        elif direction.upper() == "WEST":
                bearing = 270
        elif direction.upper() == "LEFT":
                # print commuters.get(str(commuterName))[3]
                # print str(commuterName)
                # print commuters.keys()
                if str(commuterName) in commuters:
                    commuters.get(str(commuterName))[3] = (float(commuters.get(str(commuterName))[3]) - 90.0) % 360
                    # print "called1"
                
                # print commuters.get(str(commuterName))[3]
                return commuters.get(str(commuterName))[3]

        elif direction.upper() == "RIGHT":
                if str(commuterName) in commuters:
                    commuters.get(str(commuterName))[3] = (float(commuters.get(str(commuterName))[3]) + 90.0) % 360
                return commuters.get(str(commuterName))[3]

        if str(commuterName) in commuters:
            # print "called2"
            commuters.get(str(commuterName))[3] = bearing

        return bearing
                
#turn on a specific angle clockwise
def turn_angle(angle):
        global bearing
        bearing = (bearing + angle)%360

#finds the distance (using the great circle distance) between two addresses or geo-coordinates
def compute_distance(add1, add2):
        if type(add1) is str and type(add2) is str:
                geo1 = geocode_address(add1)
                geo2 = geocode_address(add2)
        elif type(add1) is str and type (add2) is not str:
                #the address is a list [lon, lat]
                geo1 = geocode_address(add1)
                geo2 = add2
        elif type(add2) is str and type (add1) is not str:
                #the address is a list [lon, lat]
                geo1 = geocode_address(add2)
                geo2 = add1
        else:
                #the address is a list [lon, lat]
                geo1 = add1
                geo2 = add2
                
        return distFrom(geo1[1], geo1[0], geo2[1], geo2[0])


def polarToCartesian(pnt):

        lon = pnt[0]
        lat = pnt[1]
        
        r = 3959.0

        lon = math.radians(lon)
        lat = math.radians(lat)

        x = r * math.cos(lat) * math.cos(lon)
        y = r * math.cos(lat) * math.sin(lon)
        z = r * math.sin(lat)

        return x, y
        
        
#Find the projected distance between line: a,b and point c
def projectedDistance(a, b, c):
        c1 = Coordinate(a[0], a[1])
        c2 = Coordinate(b[0], b[1])
        p = Coordinate(c[0], c[1])
        segment = LineSegment(c1, c2)
        distance = segment.distancePerpendicular(p)
        return distance

#returns the perpendicular projected point on line (endPoint1, endPoint2)
def projectPointToLine( endPoint1,  endPoint2,  toProject):
        c1 = Coordinate(endPoint1[0], endPoint1[1])
        c2 = Coordinate(endPoint2[0], endPoint2[1])
        p = Coordinate(toProject[0], toProject[1])
        segment = LineSegment(c1, c2)

        pt = segment.project(p)

        return pt.x, pt.y

#checks if the point is within 0.005 miles from the line (e.g., road), also checks that the point is located within the
#endpoints of the line
def acceptPoint(endPoint1, endPoint2, p, distance):

        env1 = Coordinate(endPoint1[0], endPoint1[1])
        env2 = Coordinate(endPoint2[0], endPoint2[1])

        envelope = Envelope(env1, env2)

        pnt = Coordinate(p[0], p[1])
          
        if distance <= 0.05 and envelope.contains(pnt):
                return True
        else:
                return False

#given the start and end of a line, test whether the intersection point falls inside the interval
def test_intersection_points(start, end, lonlat_list):
        result = []
        ln1, lt1 = polarToCartesian(start)
        ln2, lt2 = polarToCartesian(end)
        for i in range(len(lonlat_list)):
                ptln, ptlt = polarToCartesian(lonlat_list[i])
                dist = projectedDistance((ln1, lt1), (ln2,lt2), (ptln, ptlt))
                projected_point = projectPointToLine ((ln1, lt1), (ln2,lt2), (ptln, ptlt))
                if acceptPoint((ln1, lt1), (ln2,lt2),projected_point, dist):
                        result.append(lonlat_list[i])

        #print result
        return result
                       
#calculates thr bearing between two geo-coordinates
def calculate_initial_compass_bearing(pointA, pointB):

    #if (type(pointA) != tuple) or (type(pointB) != tuple):
        #raise TypeError("Only tuples are supported as arguments")
 
    lat1 = math.radians(pointA[1])
    lat2 = math.radians(pointB[1])
 
    diffLong = math.radians(pointB[0] - pointA[0])
 
    x = math.sin(diffLong) * math.cos(lat2)
    y = math.cos(lat1) * math.sin(lat2) - (math.sin(lat1)
            * math.cos(lat2) * math.cos(diffLong))
 
    initial_bearing = math.atan2(x, y)
 
    # Now we have the initial bearing but math.atan2 return values
    # from -180 to + 180 which is not what we want for a compass bearing
    # The solution is to normalize the initial bearing as shown below
    initial_bearing = math.degrees(initial_bearing)
    compass_bearing = (initial_bearing + 360) % 360
 
    return compass_bearing

def cartesianToPolar(x, y):
    r = 3959.0

    z = math.sqrt(r*r - x*x - y*y)
    lat = math.asin(z / r)
    lon = math.atan2(y, x)

    lat = math.degrees(lat)
    lon = math.degrees(lon)

    return lon, lat

def test_query():
        query = "select ST_asText(ST_Simplify(the_geom, 0.05)) from tiger_data.in_zcta5 where zcta5ce = '47906'"
        #print query
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        c = db.cursor()
        c.execute(query)
        
        rowcount = c.rowcount

        if rowcount == 1:
                row = c.fetchone()
                c.close()
                db.close()

        return extract_polygon(str(row))


def test_query2():
        query = "select ST_asText(the_geom) from tiger_data.in_roads where linearid = '110168395940'"
        
        #print query
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        c = db.cursor()
        c.execute(query)
        
        rowcount = c.rowcount
        
        
        if rowcount == 1:
                row = c.fetchone()
                c.close()
                db.close()
                
                print str(row)
                return extract_polyline(str(row))

#forms a point on EWKT format: 'POINT(lon lat)'
def get_EWKT(lonlatList):
        return "'POINT (" + str(lonlatList[0])+ " " + str(lonlatList[1]) + ")'"

def  cross_product(a,  b, c):
     if ((b[0] - a[0])*(c[1] - a[1]) - (b[1] - a[1])*(c[0] - a[0])) > 0:
         return 1
     else:
         return -1

def subtract_bearing(brn, amount):
    if (brn - amount) >= 0:
        return brn - amount
    else:
        return brn - amount + 359  

#Given a distance north, return the change in latitude."
def change_in_latitude(miles):
    return (miles/earth_radius)*radians_to_degrees

#Given a latitude and a distance west, return the change in longitude.
def change_in_longitude(latitude, miles):
    
    # Find the radius of a circle around the earth at given latitude.
    r = earth_radius*math.cos(latitude*degrees_to_radians)
    return (miles/r)*radians_to_degrees


def test_query3(road, area_code, brng, lonlatList, distance):
    
    dist_degrees = change_in_latitude(distance)
    point_EWKT = get_EWKT(lonlatList)

    query = "select ST_asText( tiger_data.point_at_distance ('"+ road + "' , '" + area_code +"', " + str(brng) +  "," + point_EWKT + "," + str(dist_degrees) +"))"
    
    # print query
    
    #db = zxJDBC.connect(CONNECT_STRING, "postgres", "", "org.postgresql.Driver")
    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount

    if rowcount == 1:
        row = c.fetchone()
        c.close()
        db.close()
        return extract_line_string(str(row))
    
def call():
    query = "select ST_AsText(ST_LineMerge(a.the_geom)) from (select the_geom from tiger_data.in_roads where fullname = 'Hillcrest Rd') as a, (select the_geom from tiger_data.in_zcta5 where zcta5ce = '47906') as b   where ST_within(a.the_geom, ST_Simplify(b.the_geom,0.001))"


    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount
    
    roads = []

    for i in range(rowcount):
        st = c.fetchone()
        roads.append(str(st[0]))
    
    print roads


    c.close()
    db.close()

def call2():
    query = "select ST_AsText(ST_LineMerge(a.the_geom))   from (select the_geom from tiger_data.in_roads where fullname = 'N College Ave') as a, (select the_geom from tiger_data.in_zcta5 where zcta5ce = '46220') as b  where ST_Intersects(a.the_geom, ST_Simplify(b.the_geom,0.001)) "


    # query = "select (ST_Length(ST_Transform(ST_LineMerge(st_geometryn(a.the_geom,1)),2877))/5280) \
    #         from (select the_geom from tiger_data.in_roads where fullname = 'Sheridan Rd') as a,\
    #         (select the_geom from tiger_data.in_zcta5 where zcta5ce = '47906') as b \
    #         where ST_within(a.the_geom, ST_Simplify(b.the_geom,0.001)) LIMIT 1"

    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount
    
    roads = []

    for i in range(rowcount):
        st = c.fetchone()
        roads.append(str(st[0]))
    print "what "
    print roads
    return None



    query = "select (ST_Length(ST_Transform(ST_LineMerge(a.the_geom),2877))/5280) from (select the_geom from tiger_data.in_roads where fullname = 'Sheridan Rd') as a, (select the_geom from tiger_data.in_zcta5 where zcta5ce = '47906') as b  where ST_within(a.the_geom, ST_Simplify(b.the_geom,0.001))"


    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount
    
    roads = []

    for i in range(rowcount):
        st = c.fetchone()
        roads.append(str(st[0]))
    
    # print roads

    totalLength = float(roads[0])
    print "total length : " + str(totalLength)

    query = "select ST_Line_Locate_Point( (select ST_LineMerge(a.the_geom) from (select the_geom from tiger_data.in_roads where fullname = 'Hillcrest Rd') as a, (select the_geom from tiger_data.in_zcta5 where zcta5ce = '47906') as b  where ST_within(a.the_geom, ST_Simplify(b.the_geom,0.001))), ST_GeomFromText('POINT (-86.920866 40.439954)',4269))"


    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount
    
    roads = []

    for i in range(rowcount):
        st = c.fetchone()
        roads.append(str(st[0]))
    
    


    startFraction = float(roads[0])


    print "startFraction " + str(startFraction)

    distance = 0.1

    distanceFraction = distance / totalLength

    nextFraction1 = startFraction - distanceFraction
    nextFraction2 = startFraction + distanceFraction
    print nextFraction1
    print nextFraction2

    query = "select ST_AsText(ST_Line_Interpolate_Point( (select ST_LineMerge(a.the_geom) from (select the_geom from tiger_data.in_roads where fullname = 'Hillcrest Rd') as a, (select the_geom from tiger_data.in_zcta5 where zcta5ce = '47906') as b   where ST_within(a.the_geom, ST_Simplify(b.the_geom,0.001))), " + str(nextFraction1) + "))"

    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount
    
    roads = []

    for i in range(rowcount):
        st = c.fetchone()
        roads.append(str(st[0]))
    
    print roads

    query = "select ST_AsText(ST_Line_Interpolate_Point( (select ST_LineMerge(a.the_geom) from (select the_geom from tiger_data.in_roads where fullname = 'Hillcrest Rd') as a, (select the_geom from tiger_data.in_zcta5 where zcta5ce = '47906') as b  where ST_within(a.the_geom, ST_Simplify(b.the_geom,0.001))), " + str(nextFraction2) + "))"

    db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
    c = db.cursor()
    c.execute(query)
    rowcount = c.rowcount
    
    roads = []

    for i in range(rowcount):
        st = c.fetchone()
        roads.append(str(st[0]))
    
    print roads





    c.close()
    db.close()

def test_query4():
        #query = "select ST_asText(the_geom) from tiger_data.in_roads where linearid = '110168395940'"
        
        query = "select ST_asText(a.the_geom) from (select linearid, the_geom from tiger_data.in_primaryroads where fullname = 'I- 65') as a, (select the_geom from tiger_data.state_all where name = 'Indiana') as b where ST_within(a.the_geom, ST_Simplify(b.the_geom,0.001)) "
        
        #print query
        #db = zxJDBC.connect(CONNECT_STRING, "postgres", "", "org.postgresql.Driver")
        db = zxJDBC.connect(CONNECT_STRING, DB_USER, PASSWORD, "org.postgresql.Driver")
        
        c = db.cursor()
        c.execute(query)

        rowcount = c.rowcount
        if rowcount >1:
            lonlatList = []
            for i in range (rowcount):
                row = c.fetchone()      
                lonlatList.append(extract_polyline(str(row)))
            c.close()
            db.close()
            return lonlatList
      

