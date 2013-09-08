package unlekker.mb2.geo;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import unlekker.mb2.util.UBasic;
import unlekker.mb2.util.UFile;


public class UGeoIO {

  
  public static boolean writeSTL(String filename,UGeo model) {
    byte [] header;
    ByteBuffer buf;
    boolean res=false;
    
    try {
      if(!filename.toLowerCase().endsWith("stl")) filename+=".stl";
//      FileOutputStream out=(FileOutputStream)IO.getOutputStream(filename);
      FileOutputStream out=(FileOutputStream)UFile.getOutputStream(filename);

      buf = ByteBuffer.allocate(200);
      header=new byte[80];
      buf.get(header,0,80);
      out.write(header);
      buf.rewind();

      buf.order(ByteOrder.LITTLE_ENDIAN);
      int faceNum=model.sizeF();
      
      buf.putInt(faceNum);
      buf.rewind();
      buf.get(header,0,4);
      out.write(header,0,4);
      buf.rewind();
      
      UBasic.logDivider("Writing STL '"+filename+"' "+faceNum);

      buf.clear();
      header=new byte[50];
//      if(bb!=null) UUtil.log(bb.toString());
      
      for(UFace f:model.getFaces()) {
        UVertex v[]=f.getVertices();
        UVertex fn=f.normal();
        
        buf.rewind();
        buf.putFloat(fn.x);
        buf.putFloat(fn.y);
        buf.putFloat(fn.z);
        
        for(int j=0; j<3; j++) {
          buf.putFloat(v[j].x);
          buf.putFloat(v[j].y);
          buf.putFloat(v[j].z);
        }
        
        buf.rewind();
        buf.get(header);
        out.write(header);
      }

      out.flush();
      out.close();
      UBasic.log("Closing '"+filename+"'. "+faceNum+" triangles written.\n");
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return res;
  }
}