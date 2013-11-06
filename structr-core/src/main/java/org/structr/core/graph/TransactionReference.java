package org.structr.core.graph;

import org.neo4j.graphdb.Lock;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Transaction;

/**
 *
 * @author Christian Morgner
 */
public class TransactionReference implements Transaction {

	private Transaction tx     = null;
	private int referenceCount = 0;
	private boolean successful = false;
	
	public TransactionReference(final Transaction tx) {
		this.tx = tx;
	}
	
	public boolean isToplevel() {
		return referenceCount == 1;
	}

	public void begin() {
		
		for (int i=0; i<referenceCount; i++) { System.out.print("    "); }
		System.out.println("BEGIN");
		referenceCount++;
	}
	
	public void end() {
		referenceCount--;
		for (int i=0; i<referenceCount; i++) { System.out.print("    "); }
		System.out.println("END");
	}
	
	public int getReferenceCount() {
		return referenceCount;
	}
	
	// ----- interface Transaction -----
	@Override
	public void failure() {
		for (int i=0; i<referenceCount; i++) { System.out.print("    "); }
		System.out.println("FAILURE");
		tx.failure();
	}

	@Override
	public void success() {
		for (int i=0; i<referenceCount; i++) { System.out.print("    "); }
		System.out.println("SUCCESS");
		tx.success();
		successful = true;
	}

	@Override
	public void finish() {
		
		// only finish transaction if we are at root level
		if (--referenceCount == 0) {
			
			for (int i=0; i<referenceCount; i++) { System.out.print("    "); }
			System.out.println("FINISH");
			
			// fail transaction if no success() call was made
			if (!successful) {
				tx.failure();
			}
			
			tx.finish();
		}
	}

	@Override
	public Lock acquireWriteLock(PropertyContainer entity) {
		return tx.acquireWriteLock(entity);
	}

	@Override
	public Lock acquireReadLock(PropertyContainer entity) {
		return tx.acquireReadLock(entity);
	}
}