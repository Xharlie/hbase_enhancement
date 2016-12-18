/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hbase.client;

import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.classification.InterfaceAudience;
import org.apache.hadoop.hbase.classification.InterfaceStability;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.protobuf.generated.ClientProtos.GetResponse;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Facility class for easily customizing a get callback
 * @param <T>
 */
@InterfaceAudience.Private
@InterfaceStability.Evolving
public abstract class AsyncGetCallback implements AsyncRpcCallback<GetResponse> {

  protected final String row;
  protected HRegionLocation location;

  public AsyncGetCallback(byte[] row) {
    this.row = Bytes.toString(row);
  }

  @Override
  public void run(GetResponse response) {
    Result result = ProtobufUtil.toResult(response.getResult());
    processResult(result);
  }

  @Override
  public void onError(Throwable exception) {
    processError(exception);
  }

  abstract public void processResult(Result result);

  abstract public void processError(Throwable exception);

  @Override
  public String toString() {
    return "AsyncGetCallback_" + row;
  }

  @Override
  public int hashCode() {
    return this.row.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof AsyncGetCallback)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    return ((AsyncGetCallback) obj).row.equals(this.row);
  }

  @Override
  public void setLocation(HRegionLocation location) {
    this.location = location;
  }

}
