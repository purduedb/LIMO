from com.ziclix.python.sql import zxJDBC
 
db = zxJDBC.connect("jdbc:postgresql://10.211.55.9:5432/gisDBTest", "postgres", "", "org.postgresql.Driver")
c = db.cursor()
c.execute(
 
"SELECT ST_X(geomout), ST_Y(geomout) FROM geocode_intersection( 'ELIZABETH ST','14TH', 'IN', 'LAFAYETTE', '47904',1)"
           SELECT ST_AsText(geomout) FROM geocode_intersection('ELIZABETH ST','14TH','IN','LAFAYETTE',1)
 
 
 
)
for row in c:
  print row
c.close()
db.close()