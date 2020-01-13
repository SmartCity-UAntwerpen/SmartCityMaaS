/**
 * Robot link needs a separate class because it has no corresponding SVG element
 */
export default class Link {
    /**
     * 
     * @param {*} startNode . Id of the startnode
     * @param {*} startHeading . Heading in which the link starts
     * @param {*} destinationNode . Id of the destination node
     * @param {*} destinationHeading . Heading in which the link ends
     */
    constructor(startNode, startHeading, destinationNode, destinationHeading, distance, angle){
        this._startNode = startNode;
        this._startHeading = startHeading;
        this._destinationNode = destinationNode;
        this._destinationHeading = destinationHeading;
        this._distance = distance;
        this._angle = angle;
        this._isLocal = false;
        if(startNode === destinationNode) this._isLocal = true;
    }
    get startNode(){
        return this._startNode;
    }
    get startHeading(){
        return this._startHeading;
    }
    get destinationHeading(){
        return this._destinationHeading;
    }
    get destinationNode(){
        return this._destinationNode;
    }
    get angle(){
        return this._angle;
    }
    get distance(){
        return this._distance;
    }
    get isLocal(){
        return this._isLocal;
    }
}