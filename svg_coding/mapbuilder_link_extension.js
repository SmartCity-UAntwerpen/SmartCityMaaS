/**
 * Robot link needs a separate class because it has no corresponding SVG element
 */
export default class Link {
    /**
     * Constructor
     * @param {*} from 
     * @param {*} to 
     */
    constructor(from, to){
        this._from = from;
        this._to = to;
    }
    get from(){
        return this._from;
    }
    get to(){
        return this._to;
    }
}