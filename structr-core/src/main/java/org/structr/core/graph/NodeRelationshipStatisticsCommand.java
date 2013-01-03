/*
 *  Copyright (C) 2010-2013 Axel Morgner, structr <structr@structr.org>
 *
 *  This file is part of structr <http://structr.org>.
 *
 *  structr is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  structr is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with structr.  If not, see <http://www.gnu.org/licenses/>.
 */



package org.structr.core.graph;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import org.structr.core.entity.AbstractNode;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;
import org.structr.common.error.FrameworkException;

//~--- classes ----------------------------------------------------------------

/**
 * Returns an aggregated Map of relationship counts for the given node.
 *
 * @param one or more AbstractNode instances to collect the properties from.
 * @return a list of relationships for the given nodes
 *
 * @author Axel Morgner
 */
public class NodeRelationshipStatisticsCommand extends NodeServiceCommand {

	private static final Logger logger = Logger.getLogger(NodeRelationshipStatisticsCommand.class.getName());

	//~--- methods --------------------------------------------------------

	public Map<RelationshipType, Long> execute(AbstractNode sNode) throws FrameworkException {
		return execute(sNode, null);
	}
	
	public Map<RelationshipType, Long> execute(AbstractNode sNode, Direction dir) throws FrameworkException {

		Map<RelationshipType, Long> statistics = new LinkedHashMap<RelationshipType, Long>();
		Iterable<Relationship> rels            = null;
		Node node                              = sNode.getNode();

		if (dir != null) {
			
			rels = node.getRelationships(dir);
			
		} else {
			
			rels = node.getRelationships();
		}

		try {

			// use temporary map to avoid frequent construction of Long values when increasing..
			Map<RelationshipType, LongValueHolder> values = new LinkedHashMap<RelationshipType, LongValueHolder>();
			for (Relationship r : rels) {

				RelationshipType relType = r.getType();
				LongValueHolder count = values.get(relType);
				if(count == null) {
					count = new LongValueHolder();
					values.put(relType, count);
				}
				count.inc();
			}

			// create results from temporary map
			for(Entry<RelationshipType, LongValueHolder> entry : values.entrySet()) {
				RelationshipType key = entry.getKey();
				LongValueHolder value = entry.getValue();
				statistics.put(key, value.getValue());
			}

		} catch (RuntimeException e) {

			logger.log(Level.WARNING, "Exception occured.", e);
		}

		return statistics;
	}

	private static class LongValueHolder {
		
		private long value = 0;

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}

		public void inc() {
			this.value++;
		}
	}
}