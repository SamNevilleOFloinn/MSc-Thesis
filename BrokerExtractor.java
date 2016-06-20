package projectPackage;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.cloudbus.cloudsim.*;
//import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.core.CloudSim;

//import Cloudsim.*;
//import org.cloudbus.cloudsim.Cloudlet;

/* @author Sam O'Floinn
 *  Class BrokerExtractor
 * This class takes the traits of a VM and Cloudlet, then writes them into an .arff file.
 * It gets these traits by calling from a broker from a cloudsim simulation.
 * It should receive a DataCenterBroker object as a constructor argument, and build around an .arff data file from those arguments.
 * That file can then be passed into weka tools, which can predict the file's computation efficiency when certain machine learning tools are used.
 */

public class BrokerExtractor {
	
	/*Variables*/
	DatacenterBroker broker;
	String filename;
	
	List<Cloudlet> cloudlets;
	List<Vm> vms;
	
	/*Constructor*/
	public BrokerExtractor(DatacenterBroker bro, String name)
	{
		filename = name;
		broker = bro;
		this.writeFile();	
	}
	
	public ArrayList<Cloudlet> getCloudlets()
	{
		ArrayList<Cloudlet> newList = (ArrayList<Cloudlet>) broker.getCloudletList();
		return newList;
	}
	public ArrayList<Vm> getVms()
	{
		return (ArrayList<Vm>) broker.getVmList();
	}
	
	public void writeFile()
	{
		try {
			vms = getVms(); 
			cloudlets = getCloudlets();
			
			FileWriter fw = new FileWriter(filename + ".arff");
			PrintWriter pw = new PrintWriter(fw);
			
			System.out.println("Created " + filename);
			
			pw.println("@relation " + filename + "\n");
			pw.println("@attribute cloudletLength numeric");
			pw.println("@attribute cloudletFileSize numeric");
			pw.println("@attribute cloudletOutputSize numeric");
			pw.println("@attribute cloudletCostPerSec numeric");
			
			pw.println("@attribute vmMips numeric");
			pw.println("@attribute vmSize numeric");
			pw.println("@attribute vmRam numeric");
			pw.println("@attribute vmBw numeric");
			pw.println("@attribute vmPesNum numeric \n");
			
			System.out.println("All attributes printed. Now to print the data!");
			
			Vm currVm;
			Cloudlet currCloudlet;
			
			Random rand = new Random();
			int pred;
			
			pw.println("@data");
			for (int i = 0; i < 1; i++)
			{
				currVm = vms.get(i);
				currCloudlet = cloudlets.get(i);
				
				/*cloudlet traits: length, filesize, outputsize, costPerSec*/
				pw.print(currCloudlet.getCloudletLength() + ",");
				
				pw.print(currCloudlet.getCloudletFileSize()+",");
				
				pw.print(currCloudlet.getCloudletOutputSize() +",");
				
				pw.print(currCloudlet.getCostPerSec() + ",");
				
				//VM traits: mips, size, ram, bw, pes, vmm
				pw.print(currVm.getMips() + ",");

				pw.print(currVm.getSize() + ",");
				
				pw.print(currVm.getRam() + ",");
				
				pw.print(currVm.getBw() + ",");
				
				pw.print(currVm.getNumberOfPes() + ",");
				
				//random number between 0 and 10. If x > 3, set to yes. if x <= 3, set to no
				pred = rand.nextInt(11);
				System.out.println("Rand = " + pred);
				pw.println(getPrediction(pred));
				
				System.out.println("Current VM Requested items:");
				System.out.println("mips: " + currVm.getCurrentRequestedMips());
				System.out.println("bw allocated: " + currVm.getCurrentAllocatedBw());
				System.out.println("bw requested: " + currVm.getCurrentRequestedBw());
				System.out.println("ram: " + currVm.getCurrentRequestedRam());
				System.out.println("Size: " + currVm.getCurrentAllocatedSize());
				
				System.out.println( (i+1) + " iterations done!");
				
				System.out.println("Cloudlet submission time = " + currCloudlet.getSubmissionTime() );
				System.out.println("Cloudlet waiting time = " + currCloudlet.getWaitingTime() );
				System.out.println("Cloudlet wall clock time = " + currCloudlet.getWallClockTime() );
			}
			pw.close();
			System.out.println("Done!");
		}
		catch (Exception e){
			System.out.println("Error!"); 
		}
	}
	
	public String getPrediction(int x)
	{
		if (x > 3)
		{
			return "yes";
		}
		else
		{
			return "no";
		}
	}
	/*pseudocode for how it should look:
	 * @relation myFile.Arff
	 * @attribute VMAttr1 numeric (suppose for now that all are numeric)
	 * @attribute VMAttr2 numeric
	 * @attribute VMAttrN numeric
	 * @attribute CloudletAttr1 numeric
	 * @attribute CloudletAttrN numeric
	 * 
	 * @data [concatenate with for-loop]
	 * for (every cloudlet in the cloudlet list) [not sure if need count VMs]
	 * {	newline
	 * 		write "VMAttr1,"
	 * 		write "VMAttr2,"
	 * 		write "VMAttrN,"
	 * 		write "CloudletAttr1."
	 * 		write "CloudletAttrN"
	 * }
	 * (note: assuming all fields are filled)
	 * */
	
}
