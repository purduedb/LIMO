from com.vividsolutions.jts.geom import Coordinate 
from com.vividsolutions.jts.geom import GeometryFactory 
from com.vividsolutions.jts.geom import Envelope 
  
  
  
# first some coordinates 
coord1 = Coordinate(5, 7) 
coord2 = Coordinate(3, 6) 
coord3 = Coordinate(1, 4) 
coord4 = Coordinate(2, 5) 
coord5 = Coordinate(4, 2) 
coord6 = Coordinate(7, 2) 
coord7 = Coordinate(5, 5) 
coord8 = Coordinate(2, 6) 
coord5 = Coordinate(4, 2) 
coords = [coord1, coord2, 
          coord3, coord4, 
          coord5, coord6, 
          coord7, coord8] 
  
  
  
# the handy GeometryFactory 
geomfactx = GeometryFactory() 
  
# a MultiPoint Geometry object 
mp = geomfactx.createMultiPoint(coords) 